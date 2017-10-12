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

package org.apache.ignite.internal.processors.cache.mvcc;

import java.nio.ByteBuffer;
import java.util.Map;
import org.apache.ignite.internal.GridDirectMap;
import org.apache.ignite.plugin.extensions.communication.Message;
import org.apache.ignite.plugin.extensions.communication.MessageReader;
import org.apache.ignite.plugin.extensions.communication.MessageWriter;

/**
 *
 */
public class CoordinatorActiveQueriesMessage implements MvccCoordinatorMessage {
    /** */
    @GridDirectMap(keyType = Message.class, valueType = Integer.class)
    private Map<MvccCounter, Integer> activeQrys;

    @Override public boolean waitForCoordinatorInit() {
        return false;
    }

    @Override public boolean processedFromNioThread() {
        return true;
    }

    @Override public boolean writeTo(ByteBuffer buf, MessageWriter writer) {
        return false;
    }

    @Override public boolean readFrom(ByteBuffer buf, MessageReader reader) {
        return false;
    }

    @Override public short directType() {
        return 0;
    }

    @Override public byte fieldsCount() {
        return 0;
    }

    @Override public void onAckReceived() {

    }
}
