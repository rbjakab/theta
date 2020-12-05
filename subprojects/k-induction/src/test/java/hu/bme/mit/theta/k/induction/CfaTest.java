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
package hu.bme.mit.theta.k.induction;

import hu.bme.mit.theta.cfa.CFA;
import hu.bme.mit.theta.cfa.dsl.CfaDslManager;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;

@RunWith(value = Parameterized.class)
public class CfaTest {
	@Parameterized.Parameter(value = 0)
	public String filePath;

	@Parameterized.Parameter(value = 1)
	public boolean isSafe;

	@Parameterized.Parameters(name = "{index}: {0}, {1}")
	public static Collection<Object[]> data() {
		return Arrays.asList(new Object[][] {

				{ "src/test/resources/arithmetic-bool00.cfa", false},
				{ "src/test/resources/arithmetic-bool01.cfa", false},
				{ "src/test/resources/arithmetic-bool10.cfa", false},
				{ "src/test/resources/arithmetic-bool11.cfa", false},
				{ "src/test/resources/arithmetic-int.cfa", false},
				{ "src/test/resources/arrays.cfa", false},
				{ "src/test/resources/counter5_true.cfa", true},
				{ "src/test/resources/counter_bv_true.cfa", true},
				{ "src/test/resources/counter_bv_false.cfa", false},
				{ "src/test/resources/ifelse.cfa", false},
				{ "src/test/resources/locking.cfa", true},

				{ "D:\\SzakdogaTesztek\\eca\\Problem01_label31_true-unreach-call.c_0.cfa", true},
				{ "D:\\SzakdogaTesztek\\eca\\Problem01_label52_true-unreach-call.c_0.cfa", true},
				{ "D:\\SzakdogaTesztek\\eca\\Problem02_label26_true-unreach-call.c_0.cfa", true},
				{ "D:\\SzakdogaTesztek\\eca\\Problem02_label43_false-unreach-call.c_0.cfa", false},
				{ "D:\\SzakdogaTesztek\\eca\\Problem03_label02_true-unreach-call.c_0.cfa", true},
				{ "D:\\SzakdogaTesztek\\eca\\Problem03_label38_true-unreach-call.c_0.cfa", true},

				{ "D:\\szakdoga2\\ca\\program-verification-1\\src\\test\\resources\\ca-ex_false.cfa", false},
				{ "D:\\szakdoga2\\ca\\program-verification-1\\src\\test\\resources\\ca-ex_false_simpled.cfa", false},
				{ "D:\\szakdoga2\\ca\\program-verification-1\\src\\test\\resources\\counter5_false_simpled.cfa", false},
				{ "D:\\szakdoga2\\ca\\program-verification-1\\src\\test\\resources\\counter5_true_simpled.cfa", true},
				{ "D:\\szakdoga2\\ca\\program-verification-1\\src\\test\\resources\\gcd_true.cfa", true},
				{ "D:\\szakdoga2\\ca\\program-verification-1\\src\\test\\resources\\gcd_true_simpled.cfa", true},
				{ "D:\\szakdoga2\\ca\\program-verification-1\\src\\test\\resources\\locking_true.cfa", true},
				{ "D:\\szakdoga2\\ca\\program-verification-1\\src\\test\\resources\\locking_true_simpled.cfa", true},
				{ "D:\\szakdoga2\\ca\\program-verification-1\\src\\test\\resources\\locks_15_false.c_1.cfa", false},
				{ "D:\\szakdoga2\\ca\\program-verification-1\\src\\test\\resources\\locks_15_false.c_1_simpled.cfa", false},
				{ "D:\\szakdoga2\\ca\\program-verification-1\\src\\test\\resources\\sajat.cfa", true},

//				{ "D:\\SzakdogaTesztek\\eca\\Problem03_label26_false-unreach-call.c_0.cfa", false},	// ??
//				{ "D:\\SzakdogaTesztek\\eca\\Problem03_label13_false-unreach-call.c_0.cfa", false},	// ??
//				{ "D:\\SzakdogaTesztek\\eca\\Problem03_label50_false-unreach-call.c_0.cfa", false},	// ??
//				{ "D:\\SzakdogaTesztek\\eca\\Problem01_label33_false-unreach-call.c_0.cfa", false},	// ??
//				{ "D:\\SzakdogaTesztek\\eca\\Problem01_label32_false-unreach-call.c_0.cfa", false},	// ??
//				{ "D:\\SzakdogaTesztek\\eca\\Problem03_label21_true-unreach-call.c_0.cfa", true},	// ??
//				{ "D:\\SzakdogaTesztek\\eca\\Problem03_label18_true-unreach-call.c_0.cfa", true},	// ??

		});
	}

	@Test
	public void test() throws IOException {
		CFA cfa = CfaDslManager.createCfa(new FileInputStream(filePath));

		KInduction config = new KInduction(cfa, -1, -1);
		KInductionResult result = config.check();

		System.out.println(result);

		if (!result.isUnknown()) {
			Assert.assertEquals(isSafe, result.isSafe());
		} else {
			Assert.fail();
		}
	}
}
