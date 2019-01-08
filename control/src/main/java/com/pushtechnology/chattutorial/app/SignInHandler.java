/*******************************************************************************
 * Copyright (C) 2016 Push Technology Ltd.
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

import com.pushtechnology.diffusion.client.Diffusion;
import com.pushtechnology.diffusion.client.callbacks.ErrorReason;
import com.pushtechnology.diffusion.client.features.control.topics.MessagingControl;
import com.pushtechnology.diffusion.datatype.json.JSON;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;

public class SignInHandler implements MessagingControl.RequestHandler<JSON, JSON> {

    public SignInHandler() {
        super();
    }

    @Override
    public void onClose() {

    }

    @Override
    public void onError(ErrorReason errorReason) {

    }

    @Override
    public void onRequest(JSON request, RequestContext context, Responder<JSON> responder) {
        try {
            
            JsonParser parser = new JsonFactory().createParser(request.asInputStream());
            String username = parser.getValueAsString("username");
            String password = parser.getValueAsString("password");
            System.out.println(String.format("Username: {}; Password: {};", username,password));
            responder.respond(Diffusion.dataTypes().json().fromJsonString(
                    "{ status: 'ERROR', message: 'Username is either empty or longer than 30 characters.' }"));
        } catch (Exception e) {
            // TODO: handle exception
        }

    }
}