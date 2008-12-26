/* 
 * Copyright (c) 2007-2008, Hazel Ltd. All Rights Reserved.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at 
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
 
package com.hazelcast.nio;

import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;

import com.hazelcast.impl.Build;

public abstract class AbstractSelectionHandler implements SelectionHandler {
	public static final boolean DEBUG = Build.DEBUG;

	protected SocketChannel socketChannel;

	protected InSelector inSelector;

	protected OutSelector outSelector;

	protected Connection connection;

	protected boolean socketException = false;

	protected SelectionKey sk = null;

	public AbstractSelectionHandler(Connection connection) {
		super();
		this.connection = connection;
		this.socketChannel = connection.getSocketChannel();
		this.inSelector = InSelector.get();
		this.outSelector = OutSelector.get();
	}

	protected void handleSocketException(Exception e) {
		if (DEBUG) {
			System.out.println(Thread.currentThread().getName() + " Closing Socket. cause:  " + e);
		}
		socketException = true;
		if (DEBUG) {
			e.printStackTrace();
		}
		connection.close();
	}

	public void shutdown() {

	}

}
