/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.ignite.internal.processors.cache.tree;

import org.apache.ignite.IgniteCheckedException;
import org.apache.ignite.internal.processors.cache.KeyCacheObject;
import org.apache.ignite.internal.processors.cache.mvcc.MvccCoordinatorVersion;
import org.apache.ignite.internal.processors.cache.persistence.CacheDataRow;
import org.apache.ignite.internal.processors.cache.persistence.CacheSearchRow;
import org.apache.ignite.internal.processors.cache.persistence.tree.BPlusTree;
import org.apache.ignite.internal.processors.cache.persistence.tree.io.BPlusIO;
import org.apache.ignite.internal.util.typedef.internal.S;

/**
 *
 */
public class MvccVersionBasedSearchRow extends SearchRow implements BPlusTree.RowPredicate<CacheSearchRow, CacheDataRow> {
    /** */
    private final MvccCoordinatorVersion ver;

    /**
     * @param cacheId Cache ID.
     * @param key Key.
     * @param ver Mvcc version.
     */
    public MvccVersionBasedSearchRow(int cacheId, KeyCacheObject key, MvccCoordinatorVersion ver) {
        super(cacheId, key);

        assert ver != null;

        this.ver = ver;
    }

    /** {@inheritDoc} */
    @Override public boolean apply(BPlusTree<CacheSearchRow, CacheDataRow> tree,
        BPlusIO<CacheSearchRow> io,
        long pageAddr,
        int idx) throws IgniteCheckedException
    {
        if (ver.activeTransactions() == null)
            return true;

        RowLinkIO rowIo = (RowLinkIO)io;

        if (rowIo.getMvccUpdateTopologyVersion(pageAddr, idx) != ver.coordinatorVersion())
            return true;

        return !ver.activeTransactions().contains(ver.counter()); // TODO IGNITE-3478 sort active transactions?
    }

    /** {@inheritDoc} */
    @Override public long mvccCoordinatorVersion() {
        return ver.coordinatorVersion();
    }

    /** {@inheritDoc} */
    @Override public long mvccCounter() {
        return ver.counter();
    }

    /** {@inheritDoc} */
    @Override public String toString() {
        return S.toString(MvccVersionBasedSearchRow.class, this);
    }
}
