/*
 * Copyright 2015 Department of Computer Science and Engineering, University of Moratuwa.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *                  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package lk.ac.mrt.cse.dbs.simpleexpensemanager;

import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.control.ExpenseManager;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.control.InMemoryDemoExpenseManager;

public class ApplicationTest {
    private ExpenseManager expenseManager;

    @Before
    public void setUp() {
        expenseManager = new InMemoryDemoExpenseManager();
    }

    @Test
    public void testAddAccountOne() {
        expenseManager.addAccount("190287R", "BOC", "Kajanan S.", 10000.0);
        boolean results = expenseManager.getAccountNumbersList().contains("190287R");
        assertTrue(results);
    }

    @Test
    public void testAddAccountTwo() {
        expenseManager.addAccount("000764333098", "NSB", "Kajanan S.", 1000.0);
        boolean results = expenseManager.getAccountNumbersList().contains("000764333098");
        assertTrue(results);
    }
}