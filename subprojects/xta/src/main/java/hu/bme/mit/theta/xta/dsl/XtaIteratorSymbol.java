/*
 *  Copyright 2017 Budapest University of Technology and Economics
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package hu.bme.mit.theta.xta.dsl;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Collections;

import hu.bme.mit.theta.common.dsl.Scope;
import hu.bme.mit.theta.common.dsl.Symbol;
import hu.bme.mit.theta.xta.dsl.gen.XtaDslParser.IteratorDeclContext;

final class XtaIteratorSymbol implements Symbol {

	private final String name;
	@SuppressWarnings("unused")
	private final XtaType type;

	public XtaIteratorSymbol(final Scope scope, final IteratorDeclContext context) {
		checkNotNull(context);
		name = context.fId.getText();
		type = new XtaType(scope, context.fType, Collections.emptyList());
	}

	@Override
	public String getName() {
		return name;
	}

}
