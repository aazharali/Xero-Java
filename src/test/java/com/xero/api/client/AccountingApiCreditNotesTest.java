package com.xero.api.client;

import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import org.junit.*;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.core.Every.everyItem;


import com.xero.api.XeroApiException;
import com.xero.api.ApiClient;
import com.xero.example.CustomJsonConfig;

import com.xero.api.client.*;
import com.xero.models.accounting.*;

import com.xero.example.SampleData;

import org.threeten.bp.*;
import java.io.IOException;
import com.fasterxml.jackson.core.type.TypeReference;

import java.util.Calendar;
import java.util.Map;
import java.util.UUID;
import java.util.List;
import java.util.ArrayList;
import java.math.BigDecimal;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;

public class AccountingApiCreditNotesTest {

	CustomJsonConfig config;
	ApiClient apiClientForAccounting; 
	AccountingApi api; 

    private static boolean setUpIsDone = false;
	
	@Before
	public void setUp() {
		config = new CustomJsonConfig();
		apiClientForAccounting = new ApiClient("https://virtserver.swaggerhub.com/Xero/accounting/2.0.0",null,null,null);
		api = new AccountingApi(config);
		api.setApiClient(apiClientForAccounting);
		api.setOAuthToken(config.getConsumerKey(), config.getConsumerSecret());

        // ADDED TO MANAGE RATE LIMITS while using SwaggerHub to mock APIs
        if (setUpIsDone) {
            return;
        }

        try {
            System.out.println("Sleep for 30 seconds");
            Thread.sleep(60000);
        } catch(InterruptedException e) {
            System.out.println(e);
        }
        // do the setup
        setUpIsDone = true;
	}

	public void tearDown() {
		api = null;
		apiClientForAccounting = null;
	}

	@Test
    public void createCreditNoteTest() throws IOException {
        System.out.println("@Test - createCreditNote");
        Boolean summarizeErrors = null;
        CreditNotes creditNotes = null;
        CreditNotes response = api.createCreditNote(summarizeErrors, creditNotes);
        
        assertThat(response.getCreditNotes().get(0).getType(), is(equalTo(com.xero.models.accounting.CreditNote.TypeEnum.ACCPAYCREDIT)));
        assertThat(response.getCreditNotes().get(0).getStatus(), is(equalTo(com.xero.models.accounting.CreditNote.StatusEnum.DRAFT)));
        assertThat(response.getCreditNotes().get(0).getSubTotal(), is(equalTo(40.0)));
        assertThat(response.getCreditNotes().get(0).getSubTotal().toString(), is(equalTo("40.0")));
        assertThat(response.getCreditNotes().get(0).getTotalTax(), is(equalTo(6.0)));
        assertThat(response.getCreditNotes().get(0).getTotalTax().toString(), is(equalTo("6.0")));
        assertThat(response.getCreditNotes().get(0).getTotal(), is(equalTo(46.0)));
        assertThat(response.getCreditNotes().get(0).getTotal().toString(), is(equalTo("46.0")));
        assertThat(response.getCreditNotes().get(0).getUpdatedDateUTC(), is(equalTo(OffsetDateTime.parse("2019-03-05T11:05:02.650-08:00"))));  
        assertThat(response.getCreditNotes().get(0).getCreditNoteID(), is(equalTo(UUID.fromString("f9256f04-5a99-4680-acb9-6b4639cc439a"))));
        assertThat(response.getCreditNotes().get(0).getCurrencyRate(), is(equalTo(1.0)));
        assertThat(response.getCreditNotes().get(0).getCurrencyRate().toString(), is(equalTo("1.0")));
        assertThat(response.getCreditNotes().get(0).getRemainingCredit(), is(equalTo(46.0)));
        assertThat(response.getCreditNotes().get(0).getRemainingCredit().toString(), is(equalTo("46.0")));
        assertThat(response.getCreditNotes().get(0).getValidationErrors().get(0).getMessage(), is(equalTo("An existing Credit Note with the specified CreditNoteID could not be found")));
        assertThat(response.getCreditNotes().get(0).getCurrencyCode(), is(equalTo(com.xero.models.accounting.CurrencyCode.NZD)));
        assertThat(response.getCreditNotes().get(0).getLineAmountTypes(), is(equalTo(com.xero.models.accounting.LineAmountTypes.EXCLUSIVE)));
        assertThat(response.getCreditNotes().get(0).getLineItems().get(0).getDescription(), is(equalTo("Foobar")));
        assertThat(response.getCreditNotes().get(0).getLineItems().get(0).getQuantity(), is(equalTo(2.0)));
        assertThat(response.getCreditNotes().get(0).getLineItems().get(0).getUnitAmount(), is(equalTo(20.0)));
        assertThat(response.getCreditNotes().get(0).getLineItems().get(0).getTaxType(), is(equalTo("INPUT2")));
        assertThat(response.getCreditNotes().get(0).getLineItems().get(0).getAccountCode(), is(equalTo("400")));
        assertThat(response.getCreditNotes().get(0).getLineItems().get(0).getTaxAmount(), is(equalTo(6.0)));
        assertThat(response.getCreditNotes().get(0).getLineItems().get(0).getLineAmount(), is(equalTo(40.0)));
        //System.out.println(response.getCreditNotes().get(0).toString());
    }

    @Test
    public void createCreditNoteAllocationTest() throws IOException {
        System.out.println("@Test - createCreditNoteAllocation");
        UUID creditNoteID = UUID.fromString("8138a266-fb42-49b2-a104-014b7045753d");  
        Allocations allocations = null;
        Allocations response = api.createCreditNoteAllocation(creditNoteID, allocations);
        
        assertThat(response.getAllocations().get(0).getAmount(), is(equalTo(1.0)));
        assertThat(response.getAllocations().get(0).getDate(), is(equalTo(LocalDate.of(2019, 03, 04))));
        //System.out.println(response.getAllocations().get(0).toString());
    }
    
    @Test
    public void createCreditNoteAttachmentByFileNameTest() throws IOException {
        System.out.println("@Test - createCreditNoteAttachmentByFileName");
        UUID creditNoteID = UUID.fromString("8138a266-fb42-49b2-a104-014b7045753d");  
        String fileName = "sample5.jpg";
        InputStream inputStream = CustomJsonConfig.class.getResourceAsStream("/helo-heros.jpg");
        byte[] body = IOUtils.toByteArray(inputStream);
        Attachments response = api.createCreditNoteAttachmentByFileName(creditNoteID, fileName, body);
        
        assertThat(response.getAttachments().get(0).getAttachmentID(), is(equalTo(UUID.fromString("91bbae3f-5de5-4e3d-875f-8662f25897bd"))));
        assertThat(response.getAttachments().get(0).getFileName(), is(equalTo("sample5.jpg")));
        assertThat(response.getAttachments().get(0).getMimeType(), is(equalTo("image/jpg")));
        assertThat(response.getAttachments().get(0).getUrl(), is(equalTo("https://api.xero.com/api.xro/2.0/CreditNotes/249f15fa-f2a7-4acc-8769-0984103f2225/Attachments/sample5.jpg")));
        assertThat(response.getAttachments().get(0).getContentLength(), is(equalTo(new BigDecimal("2878711"))));
        assertThat(response.getAttachments().get(0).getIncludeOnline(), is(equalTo(null)));
        //System.out.println(response.getAttachments().get(0).toString());
    }
    

    @Test
    public void createCreditNoteHistoryTest() throws IOException {
        System.out.println("@Test - createCreditNoteHistory");
        UUID creditNoteID = UUID.fromString("8138a266-fb42-49b2-a104-014b7045753d");  
        HistoryRecords historyRecords = null;
        HistoryRecords response = api.createCreditNoteHistory(creditNoteID, historyRecords);
        
        assertThat(response.getHistoryRecords().get(0).getDetails(), is(equalTo("Hello World")));     
        assertThat(response.getHistoryRecords().get(0).getDateUTC(), is(equalTo(OffsetDateTime.parse("2019-03-05T15:29:04.585-08:00"))));  
        //System.out.println(response.getHistoryRecords().get(0).toString());
    }

    @Test
    public void getCreditNoteTest() throws IOException {
        System.out.println("@Test - getCreditNote");
        UUID creditNoteID = UUID.fromString("8138a266-fb42-49b2-a104-014b7045753d");  
        CreditNotes response = api.getCreditNote(creditNoteID);
        
        assertThat(response.getCreditNotes().get(0).getType(), is(equalTo(com.xero.models.accounting.CreditNote.TypeEnum.ACCRECCREDIT)));
        assertThat(response.getCreditNotes().get(0).getDate(), is(equalTo(LocalDate.of(2019, 03, 04))));  
        assertThat(response.getCreditNotes().get(0).getStatus(), is(equalTo(com.xero.models.accounting.CreditNote.StatusEnum.AUTHORISED)));
        assertThat(response.getCreditNotes().get(0).getLineAmountTypes(), is(equalTo(com.xero.models.accounting.LineAmountTypes.EXCLUSIVE)));
        assertThat(response.getCreditNotes().get(0).getSubTotal(), is(equalTo(30.0)));
        assertThat(response.getCreditNotes().get(0).getSubTotal().toString(), is(equalTo("30.0")));
        assertThat(response.getCreditNotes().get(0).getTotalTax(), is(equalTo(4.5)));
        assertThat(response.getCreditNotes().get(0).getTotalTax().toString(), is(equalTo("4.5")));
        assertThat(response.getCreditNotes().get(0).getTotal(), is(equalTo(34.5)));
        assertThat(response.getCreditNotes().get(0).getTotal().toString(), is(equalTo("34.5")));
        assertThat(response.getCreditNotes().get(0).getUpdatedDateUTC(), is(equalTo(OffsetDateTime.parse("2019-03-05T10:59:06.157-08:00"))));  
        assertThat(response.getCreditNotes().get(0).getCurrencyCode(), is(equalTo(com.xero.models.accounting.CurrencyCode.NZD)));
        assertThat(response.getCreditNotes().get(0).getCreditNoteID(), is(equalTo(UUID.fromString("249f15fa-f2a7-4acc-8769-0984103f2225"))));
        assertThat(response.getCreditNotes().get(0).getCreditNoteNumber(), is(equalTo("CN-0005")));
        assertThat(response.getCreditNotes().get(0).getReference(), is(equalTo("US Tour")));
        assertThat(response.getCreditNotes().get(0).getCurrencyRate(), is(equalTo(1.0)));
        assertThat(response.getCreditNotes().get(0).getCurrencyRate().toString(), is(equalTo("1.0")));
        assertThat(response.getCreditNotes().get(0).getRemainingCredit(), is(equalTo(32.5)));
        assertThat(response.getCreditNotes().get(0).getRemainingCredit().toString(), is(equalTo("32.5")));
        assertThat(response.getCreditNotes().get(0).getHasAttachments(), is(equalTo(true)));
        assertThat(response.getCreditNotes().get(0).getPayments().get(0).getReference(), is(equalTo("Too much")));
        assertThat(response.getCreditNotes().get(0).getPayments().get(0).getDate(), is(equalTo(LocalDate.of(2019, 03, 13))));
        assertThat(response.getCreditNotes().get(0).getPayments().get(0).getCurrencyRate(), is(equalTo(1.0)));
        assertThat(response.getCreditNotes().get(0).getPayments().get(0).getAmount(), is(equalTo(2.0)));
        assertThat(response.getCreditNotes().get(0).getPayments().get(0).getPaymentID(), is(equalTo(UUID.fromString("6b037c9b-2e5d-4905-84d3-eabfb3438242"))));
        assertThat(response.getCreditNotes().get(0).getPayments().get(0).getHasAccount(), is(equalTo(false)));
        assertThat(response.getCreditNotes().get(0).getPayments().get(0).getHasValidationErrors(), is(equalTo(false)));
        //System.out.println(response.getCreditNotes().get(0).toString());
    }

    @Test
    public void getCreditNoteAttachmentsTest() throws IOException {
        System.out.println("@Test - getCreditNoteAttachments");
        UUID creditNoteID = UUID.fromString("8138a266-fb42-49b2-a104-014b7045753d");  
        Attachments response = api.getCreditNoteAttachments(creditNoteID);
        
        assertThat(response.getAttachments().get(0).getAttachmentID(), is(equalTo(UUID.fromString("b7eb1fc9-a0f9-4e8e-9373-6689f5350832"))));
        assertThat(response.getAttachments().get(0).getFileName(), is(equalTo("HelloWorld.png")));
        assertThat(response.getAttachments().get(0).getMimeType(), is(equalTo("image/png")));
        assertThat(response.getAttachments().get(0).getUrl(), is(equalTo("https://api.xero.com/api.xro/2.0/CreditNotes/249f15fa-f2a7-4acc-8769-0984103f2225/Attachments/HelloWorld.png")));
        assertThat(response.getAttachments().get(0).getContentLength(), is(equalTo(new BigDecimal("76091"))));
        assertThat(response.getAttachments().get(0).getIncludeOnline(), is(equalTo(null)));  
        //System.out.println(response.getAttachments().get(0).toString());
    }
    
    @Test
    public void getCreditNoteHistoryTest() throws IOException {
        System.out.println("@Test - getCreditNoteHistory");
        UUID creditNoteID = UUID.fromString("8138a266-fb42-49b2-a104-014b7045753d");  
        HistoryRecords response = api.getCreditNoteHistory(creditNoteID);
        
        assertThat(response.getHistoryRecords().get(0).getUser(), is(equalTo("Sidney Maestre")));       
        assertThat(response.getHistoryRecords().get(0).getChanges(), is(equalTo("Cash Refunded")));     
        assertThat(response.getHistoryRecords().get(0).getDetails(), is(equalTo("Payment made to Liam Gallagher on 14 March 2019 for 2.00. There is 32.50 credit remaining on this credit note.")));     
        assertThat(response.getHistoryRecords().get(0).getDateUTC(), is(equalTo(OffsetDateTime.parse("2019-03-05T10:59:06.157-08:00"))));  
        //System.out.println(response.getHistoryRecords().get(0).toString());
    }
    

    @Test
    public void getCreditNotesTest() throws IOException {
        System.out.println("@Test - getCreditNotes");
        OffsetDateTime ifModifiedSince = null;
        String where = null;
        String order = null;
        Integer page = null;
        CreditNotes response = api.getCreditNotes(ifModifiedSince, where, order, page);

        assertThat(response.getCreditNotes().get(0).getType(), is(equalTo(com.xero.models.accounting.CreditNote.TypeEnum.ACCRECCREDIT)));
        assertThat(response.getCreditNotes().get(0).getDate(), is(equalTo(LocalDate.of(2019, 03, 04))));  
        assertThat(response.getCreditNotes().get(0).getStatus(), is(equalTo(com.xero.models.accounting.CreditNote.StatusEnum.AUTHORISED)));
        assertThat(response.getCreditNotes().get(0).getLineAmountTypes(), is(equalTo(com.xero.models.accounting.LineAmountTypes.EXCLUSIVE)));
        assertThat(response.getCreditNotes().get(0).getSubTotal(), is(equalTo(30.0)));
        assertThat(response.getCreditNotes().get(0).getSubTotal().toString(), is(equalTo("30.0")));
        assertThat(response.getCreditNotes().get(0).getTotalTax(), is(equalTo(4.5)));
        assertThat(response.getCreditNotes().get(0).getTotalTax().toString(), is(equalTo("4.5")));
        assertThat(response.getCreditNotes().get(0).getTotal(), is(equalTo(34.5)));
        assertThat(response.getCreditNotes().get(0).getTotal().toString(), is(equalTo("34.5")));
        assertThat(response.getCreditNotes().get(0).getUpdatedDateUTC(), is(equalTo(OffsetDateTime.parse("2019-03-05T10:59:06.157-08:00"))));  
        assertThat(response.getCreditNotes().get(0).getCurrencyCode(), is(equalTo(com.xero.models.accounting.CurrencyCode.NZD)));
        assertThat(response.getCreditNotes().get(0).getCreditNoteID(), is(equalTo(UUID.fromString("249f15fa-f2a7-4acc-8769-0984103f2225"))));
        assertThat(response.getCreditNotes().get(0).getCreditNoteNumber(), is(equalTo("CN-0005")));
        assertThat(response.getCreditNotes().get(0).getReference(), is(equalTo("US Tour")));
        assertThat(response.getCreditNotes().get(0).getCurrencyRate(), is(equalTo(1.0)));
        assertThat(response.getCreditNotes().get(0).getCurrencyRate().toString(), is(equalTo("1.0")));
        assertThat(response.getCreditNotes().get(0).getRemainingCredit(), is(equalTo(32.5)));
        assertThat(response.getCreditNotes().get(0).getRemainingCredit().toString(), is(equalTo("32.5")));
        assertThat(response.getCreditNotes().get(0).getHasAttachments(), is(equalTo(true)));
        assertThat(response.getCreditNotes().get(0).getPayments().get(0).getReference(), is(equalTo("Too much")));
        assertThat(response.getCreditNotes().get(0).getPayments().get(0).getDate(), is(equalTo(LocalDate.of(2019, 03, 13))));
        assertThat(response.getCreditNotes().get(0).getPayments().get(0).getCurrencyRate(), is(equalTo(1.0)));
        assertThat(response.getCreditNotes().get(0).getPayments().get(0).getAmount(), is(equalTo(2.0)));
        assertThat(response.getCreditNotes().get(0).getPayments().get(0).getPaymentID(), is(equalTo(UUID.fromString("6b037c9b-2e5d-4905-84d3-eabfb3438242"))));
        assertThat(response.getCreditNotes().get(0).getPayments().get(0).getHasAccount(), is(equalTo(false)));
        assertThat(response.getCreditNotes().get(0).getPayments().get(0).getHasValidationErrors(), is(equalTo(false)));
        //System.out.println(response.getHistoryRecords().get(0).toString());
    }

    @Test
    public void updateCreditNoteTest() throws IOException {
        System.out.println("@Test - updateCreditNote");
        UUID creditNoteID = UUID.fromString("8138a266-fb42-49b2-a104-014b7045753d");  
        CreditNotes creditNotes = null;
        CreditNotes response = api.updateCreditNote(creditNoteID, creditNotes);

        assertThat(response.getCreditNotes().get(0).getType(), is(equalTo(com.xero.models.accounting.CreditNote.TypeEnum.ACCPAYCREDIT)));
        assertThat(response.getCreditNotes().get(0).getDate(), is(equalTo(LocalDate.of(2019, 01, 04))));
        assertThat(response.getCreditNotes().get(0).getStatus(), is(equalTo(com.xero.models.accounting.CreditNote.StatusEnum.AUTHORISED)));
        assertThat(response.getCreditNotes().get(0).getLineAmountTypes(), is(equalTo(com.xero.models.accounting.LineAmountTypes.EXCLUSIVE)));
        assertThat(response.getCreditNotes().get(0).getLineItems().get(0).getDescription(), is(equalTo("Foobar")));
        assertThat(response.getCreditNotes().get(0).getLineItems().get(0).getQuantity(), is(equalTo(2.0)));
        assertThat(response.getCreditNotes().get(0).getLineItems().get(0).getUnitAmount(), is(equalTo(20.0)));
        assertThat(response.getCreditNotes().get(0).getLineItems().get(0).getTaxType(), is(equalTo("INPUT2")));
        assertThat(response.getCreditNotes().get(0).getLineItems().get(0).getAccountCode(), is(equalTo("400")));
        assertThat(response.getCreditNotes().get(0).getLineItems().get(0).getTaxAmount(), is(equalTo(6.0)));
        assertThat(response.getCreditNotes().get(0).getLineItems().get(0).getLineAmount(), is(equalTo(40.0)));
        assertThat(response.getCreditNotes().get(0).getSubTotal(), is(equalTo(40.0)));
        assertThat(response.getCreditNotes().get(0).getSubTotal().toString(), is(equalTo("40.0")));
        assertThat(response.getCreditNotes().get(0).getTotalTax(), is(equalTo(6.0)));
        assertThat(response.getCreditNotes().get(0).getTotalTax().toString(), is(equalTo("6.0")));
        assertThat(response.getCreditNotes().get(0).getTotal(), is(equalTo(46.0)));
        assertThat(response.getCreditNotes().get(0).getTotal().toString(), is(equalTo("46.0")));
        assertThat(response.getCreditNotes().get(0).getUpdatedDateUTC(), is(equalTo(OffsetDateTime.parse("2019-03-05T11:05:04.223-08:00"))));  
        assertThat(response.getCreditNotes().get(0).getCurrencyCode(), is(equalTo(com.xero.models.accounting.CurrencyCode.NZD)));
        assertThat(response.getCreditNotes().get(0).getCreditNoteID(), is(equalTo(UUID.fromString("f9256f04-5a99-4680-acb9-6b4639cc439a"))));
        assertThat(response.getCreditNotes().get(0).getReference(), is(equalTo("HelloWorld")));  
        assertThat(response.getCreditNotes().get(0).getCurrencyRate(), is(equalTo(1.0)));
        assertThat(response.getCreditNotes().get(0).getCurrencyRate().toString(), is(equalTo("1.0")));
        assertThat(response.getCreditNotes().get(0).getRemainingCredit(), is(equalTo(46.0)));
        assertThat(response.getCreditNotes().get(0).getRemainingCredit().toString(), is(equalTo("46.0")));
        //System.out.println(response.getCreditNotes().get(0).toString());
    }
    
    @Test
    public void updateCreditNoteAttachmentByFileNameTest() throws IOException {
        System.out.println("@Test - updateCreditNoteAttachmentByFileName");
        UUID creditNoteID = UUID.fromString("8138a266-fb42-49b2-a104-014b7045753d");  
        String fileName = "sample5.jpg";
        InputStream inputStream = CustomJsonConfig.class.getResourceAsStream("/helo-heros.jpg");
        byte[] body = IOUtils.toByteArray(inputStream);
        Attachments response = api.updateCreditNoteAttachmentByFileName(creditNoteID, fileName, body);
        
        assertThat(response.getAttachments().get(0).getAttachmentID(), is(equalTo(UUID.fromString("103e49f1-e47c-4b4d-b5e8-77d9d00fa70a"))));
        assertThat(response.getAttachments().get(0).getFileName(), is(equalTo("HelloWorld.jpg")));
        assertThat(response.getAttachments().get(0).getMimeType(), is(equalTo("image/jpg")));
        assertThat(response.getAttachments().get(0).getUrl(), is(equalTo("https://api.xero.com/api.xro/2.0/CreditNotes/249f15fa-f2a7-4acc-8769-0984103f2225/Attachments/HelloWorld.jpg")));
        assertThat(response.getAttachments().get(0).getContentLength(), is(equalTo(new BigDecimal("2878711"))));
        assertThat(response.getAttachments().get(0).getIncludeOnline(), is(equalTo(null)));  
        //System.out.println(response.getAttachments().get(0).toString());
    }
}
