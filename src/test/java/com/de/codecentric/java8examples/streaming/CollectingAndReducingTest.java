package com.de.codecentric.java8examples.streaming;


import com.de.codecentric.java8examples.Invoice;
import com.de.codecentric.java8examples.InvoiceItem;
import com.de.codecentric.java8examples.Person;
import com.de.codecentric.java8examples.TestData;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.*;

import static junit.framework.Assert.assertNotNull;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 * Tests for mapping and filtering feature of the streaming api. Once you have completed the stub-methods in
 * {@link FilteringAndMapping}, these tests should pass.
 */
public class CollectingAndReducingTest {
    private List<Person> persons = TestData.listOfPersons();
    private List<Invoice> invoices = TestData.listOfInvoices();
    private List<String> recipients = Arrays.asList("Homer", "Bart", "Marge");

    @Test
    public void testAverageAge() throws Exception {
        assertThat(
                CollectingAndReducing.averageAge(persons),
                closeTo(30.57D, 0.01));
    }

    @Test
    public void testMaxAge() throws Exception {
        assertThat(
                CollectingAndReducing.maxAge(persons),
                equalTo(46));
    }

    @Test
    public void testAgeStatistics() throws Exception {
        IntSummaryStatistics statistic = CollectingAndReducing.ageStatistics(persons);
        assertNotNull(statistic);
        assertThat(statistic.getAverage(), equalTo(30.571428571428573));
        assertThat(statistic.getCount(), equalTo(7l));
        assertThat(statistic.getMax(), equalTo(46));
        assertThat(statistic.getMin(), equalTo(1));
        assertThat(statistic.getSum(), equalTo(214l));
    }

    @Test
    public void testBuildCommaSeparatedListOfFirstNames() throws Exception {
        assertThat(
                CollectingAndReducing.buildCommaSeparatedListOfFirstNames(persons),
                equalTo("Jane, Mary, John, Michael, Chris, Pete, Maggie"));
    }

    @Test
    public void testCheapestProduct() throws Exception {
        assertThat(
                CollectingAndReducing.cheapestProduct(invoices),
                equalTo("Chips"));
    }

    @Test
    public void testMostExpensiveInvoice() throws Exception {
        assertThat(
                CollectingAndReducing.mostExpensiveInvoice(invoices),
                equalTo(invoices.get(2)));

    }

    @Test
    public void testGroupInvoicesByRecipient() throws Exception {
        Map<String, List<Invoice>> invoicesByRecipient =
                CollectingAndReducing.groupInvoicesByRecipient(invoices);
        assertThat(invoicesByRecipient.keySet(), hasSize(recipients.size()));

        for (String recipient: recipients) {
            for (Invoice invoice: invoices) {
                if (recipient.equals(invoice.getRecipient())) {
                    assertThat(invoicesByRecipient.get(recipient),
                            hasItem(invoice));
                } else {
                    assertThat(invoicesByRecipient.get(recipient),
                        not(hasItem(invoice)));
                }
            }
        }
    }

    @Test
    public void testExpensesByRecipient() throws Exception {
        Map<String, BigDecimal> expencesByRecipient =
                CollectingAndReducing.expensesByRecipient(invoices);
        assertThat(expencesByRecipient.keySet(), hasSize(recipients.size()));
        for (String recipient: recipients) {
            BigDecimal expenses = BigDecimal.ZERO;
            for (Invoice invoice: invoices) {
                if (recipient.equals(invoice.getRecipient())) {
                    expenses = expenses.add(invoice.getTotal());
                }
            }
            assertThat(expencesByRecipient.get(recipient), equalTo(expenses));
        }
    }

    @Test
    public void testCountByProduct() throws Exception {
        Map<String, Integer> expected = new HashMap<>();
        for (Invoice invoice: invoices) {
            for (InvoiceItem item: invoice.getItems()) {
                String product = item.getProduct();
                if (expected.get(product) == null) {
                    expected.put(product, Integer.valueOf(0));
                }
                expected.put(
                        product,
                        expected.get(product) + item.getQuantity());
            }
        }

        Map<String, Integer> actual = CollectingAndReducing.countByProduct(invoices);
        assertThat(actual.keySet(), hasSize(expected.size()));
        for (Map.Entry entry: expected.entrySet()) {
            assertThat(actual, hasEntry(entry.getKey(), entry.getValue()));
        }
    }

    @Test
    public void testCheapestDealersByProduct() throws Exception {

    }

    @Test
    public void testComputeDealerInventory() throws Exception {
        HashMap<String, List<CollectingAndReducing.ProductWithPrice>> expected = new HashMap<>();
        for (Invoice invoice: invoices) {
            String sender = invoice.getSender();
            if (expected.get(sender) == null) {
                expected.put(sender, new ArrayList<CollectingAndReducing.ProductWithPrice>());
            }
            List<CollectingAndReducing.ProductWithPrice> itemsOfSender = expected.get(sender);
            for (InvoiceItem item: invoice.getItems()) {
                CollectingAndReducing.ProductWithPrice newItem = new CollectingAndReducing.ProductWithPrice(item.getProduct(), item.getPricePerUnit());
                if (!itemsOfSender.contains(newItem)) {
                    itemsOfSender.add(newItem);
                }
            }
        }

        Map<String, List<CollectingAndReducing.ProductWithPrice>> actual =
                CollectingAndReducing.computeDealerInventory(invoices);

        assertThat(actual.keySet(), hasSize(expected.size()));
        for (String sender: expected.keySet()) {
            assertThat("Unexpected item set for dealer " + sender, actual.get(sender), containsInAnyOrder(expected.get(sender).toArray()));
        }
    }

    @Test
    public void testFavoriteArticlesByBuyer() throws Exception {

    }
}
