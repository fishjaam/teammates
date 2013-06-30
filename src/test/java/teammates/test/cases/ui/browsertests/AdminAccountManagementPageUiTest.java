package teammates.test.cases.ui.browsertests;

import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertNotNull;
import static org.testng.AssertJUnit.assertNull;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import teammates.common.Common;
import teammates.common.Url;
import teammates.common.datatransfer.DataBundle;
import teammates.test.driver.BackDoor;
import teammates.test.pageobjects.AdminAccountDetailsPage;
import teammates.test.pageobjects.AdminAccountManagementPage;
import teammates.test.pageobjects.Browser;
import teammates.test.pageobjects.BrowserPool;

/**
 * Covers the 'accounts management' view for admins.
 * SUT: {@link AdminAccountManagementPage}
 */
public class AdminAccountManagementPageUiTest extends BaseUiTestCase{
	private static Browser browser;
	private static Url accountsPageUrl;
	private static AdminAccountManagementPage accountsPage;
	private static DataBundle testData;
	
	@BeforeClass
	public static void classSetup() throws Exception {
		printTestClassHeader();
		testData = loadDataBundle("/AdminAccountManagementPageUiTest.json");
		restoreTestDataOnServer(testData);
		browser = BrowserPool.getBrowser();
	}
	
	@Test
	public void testAll(){
		testContent();
		//no input validation to check
		testViewAccountDetailsLink();
		testDeleteInstructorStatusAction();
		testDeleteInstructorAccountAction();
	}
	
	public void testContent() {
		
		______TS("content: typical page");
		
		loginToAdminAccountsManagementPage();
		accountsPage.verifyIsCorrectPage();
		//Full page content cannot be checked because the list of 
		//  account cannot be fixed in the live server. 
	}

	public void testViewAccountDetailsLink() {

		______TS("link: view account details");
		
		String instructorId = "AAMgtUiT.instr1";
		AdminAccountDetailsPage detailsPage = accountsPage.clickViewInstructorDetails(instructorId);
		detailsPage.verifyIsCorrectPage(instructorId);
		detailsPage.goToPreviousPage(AdminAccountManagementPage.class);
	}

	public void testDeleteInstructorStatusAction(){
		
		______TS("action: delete instructor status");
		
		String idOfInstructorToDelete = "AAMgtUiT.instr1";
		accountsPage.clickDeleteInstructorStatus(idOfInstructorToDelete)
			.verifyStatus(Common.MESSAGE_INSTRUCTOR_STATUS_DELETED);
		assertEquals(false, BackDoor.getAccount(idOfInstructorToDelete).isInstructor);
	}

	public void testDeleteInstructorAccountAction(){
		
		______TS("action: delete account");
		
		String googleId = "AAMgtUiT.instr3";
		accountsPage.clickAndCancelDeleteAccountLink(googleId);
		assertNotNull(BackDoor.getAccount(googleId));
		
		accountsPage.clickAndConfirmDeleteAccountLink(googleId);
		assertNull(BackDoor.getAccount(googleId));
		
	}

	private void loginToAdminAccountsManagementPage() {
		accountsPageUrl = new Url(Common.PAGE_ADMIN_ACCOUNT_MANAGEMENT);
		accountsPage = loginAdminToPage(browser, accountsPageUrl, AdminAccountManagementPage.class);
	}

	@AfterClass
	public static void classTearDown() throws Exception {
		BrowserPool.release(browser);
	}
	
}
