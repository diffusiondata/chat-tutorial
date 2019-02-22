/*******************************************************************************
 * Copyright (C) 2019 Push Technology Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/

package com.pushtechnology.chattutorial.app;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Control Client for Diffusion Chat Demo.
 *
 */
public final class ChatControlClient {
    private static final Logger LOG = LoggerFactory.getLogger(ChatControlClient.class);

    public static void main(String[] args) {
        try {
            LOG.info("Starting Control Client.");
            SignInMessageReceiver.listeningForMessages();
        } catch (Exception e) {
            LOG.error("Execution failed, application stopping.", e);
        }
    }
}