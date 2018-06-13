/*
 * Copyright (c) 2008-2018, Hazelcast, Inc. All Rights Reserved.
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
 */

package com.hazelcast.test.starter;

import java.lang.reflect.Method;

import static com.hazelcast.test.starter.ReflectionUtils.getFieldValueReflectively;

/**
 * Constructor for {@link com.hazelcast.version.Version} class proxies
 */
public class VersionConstructor extends AbstractStarterObjectConstructor {

    public VersionConstructor(Class<?> targetClass) {
        super(targetClass);
    }

    @Override
    Object createNew0(Object delegate) throws Exception {
        ClassLoader starterClassLoader = targetClass.getClassLoader();
        Class<?> versionClass = starterClassLoader.loadClass("com.hazelcast.version.Version");
        Method versionOf = versionClass.getDeclaredMethod("of", Integer.TYPE, Integer.TYPE);

        Byte major = (Byte) getFieldValueReflectively(delegate, "major");
        Byte minor = (Byte) getFieldValueReflectively(delegate, "minor");

        Object[] args = new Object[]{major.intValue(), minor.intValue()};

        return versionOf.invoke(null, args);
    }
}
