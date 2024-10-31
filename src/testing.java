package asuHelloWorldJavaFX;

import java.util.List;

/**
 * <p> TestAutomationNonUI Class </p>
 * 
 * <p> Description: Automated testing for non-UI functionalities, covering unique ID assignment, 
 * grouping, and backup/restore operations. </p>
 * 
 * @authors 
 * Neeharika Mandadapu, Genelle Jenkins, Siddharth Sanjay, Krutarth Thakkar, Monil Patel
 * 
 * @version 1.00        2024-10-30 Initial non-UI testing automation
 */
public class testing {

    /** Counter for passed tests */
    static int numPassed = 0;

    /** Counter for failed tests */
    static int numFailed = 0;

    /**
     * Main method to run all non-UI test cases.
     */
    public static void main(String[] args) {
        System.out.println("____________________________________________________________________________");
        System.out.println("\nNon-UI Testing Automation");

        performTestCase(1, "Unique Article ID Assignment", true, testUniqueIdAssignment());
        performTestCase(2, "Article Grouping Mechanism", true, testArticleGrouping());
        performTestCase(3, "Backup Articles by Group", true, testBackupByGroup());
        performTestCase(4, "Restore Articles - Merge Mode", true, testRestoreMergeMode());
        performTestCase(5, "Restore Articles - Remove All Mode", true, testRestoreRemoveAllMode());
        performTestCase(6, "Save Help Article Functionality", true, testSaveHelpArticleFunctionality());

        System.out.println("____________________________________________________________________________");
        System.out.println();
        System.out.println("Number of tests passed: " + numPassed);
        System.out.println("Number of tests failed: " + numFailed);
    }

    /**
     * Executes a single test case and logs the result.
     */
    private static void performTestCase(int testCase, String description, boolean expectedPass, boolean actualPass) {
        System.out.println("____________________________________________________________________________\n\nTest case: " + testCase);
        System.out.println("Description: " + description);
        System.out.println("Expected: " + (expectedPass ? "Pass" : "Fail"));
        System.out.println("Actual: " + (actualPass ? "Pass" : "Fail"));

        if (actualPass == expectedPass) {
            System.out.println("***Success***");
            numPassed++;
        } else {
            System.out.println("***Failure***");
            numFailed++;
        }
    }

    /**
     * Verifies unique ID assignment for articles.
     */
    private static boolean testUniqueIdAssignment() {
        try {
            long id1 = ArticleUtils.generateRandomId();
            long id2 = ArticleUtils.generateRandomId();
            return id1 != id2; // Ensure IDs are unique
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Verifies article grouping mechanism.
     */
    private static boolean testArticleGrouping() {
        try {
            // Creating a new article with multiple groups
            CreateHelpArticlePage createPage = new CreateHelpArticlePage(null);
            createPage.saveHelpArticle("Grouped Article", "Body", "Keywords", "Group1,Group2", "Description", "Beginner", "Links");

            // Fetch articles by group using ViewHelpArticlesPage
            ViewHelpArticlesPage viewPage = new ViewHelpArticlesPage(null, "Admin");
            List<HelpArticle> group1Articles = viewPage.fetchHelpArticlesByGroup("Group1");
            List<HelpArticle> group2Articles = viewPage.fetchHelpArticlesByGroup("Group2");

            boolean inGroup1 = group1Articles.stream().anyMatch(a -> a.getTitle().equals("Grouped Article"));
            boolean inGroup2 = group2Articles.stream().anyMatch(a -> a.getTitle().equals("Grouped Article"));

            return inGroup1 && inGroup2;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Verifies backup functionality by group.
     */
    private static boolean testBackupByGroup() {
        try {
            BackupService backupService = new BackupService();
            backupService.backupByGroup("Group1", "group1_backup.json"); // Backup articles in "Group1"

            // Assuming verifyBackupFile checks file contents for existence
            return backupService.verifyBackupFile("group1_backup.json");
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Verifies restore functionality in merge mode.
     */
    private static boolean testRestoreMergeMode() {
        try {
            BackupService backupService = new BackupService();
            backupService.restore("group1_backup.json", true); // Restore in merge mode

            // Fetch articles to confirm they are merged with existing ones
            ViewHelpArticlesPage viewPage = new ViewHelpArticlesPage(null, "Admin");
            List<HelpArticle> articles = viewPage.fetchHelpArticlesByGroup("Group1");

            // Validate some entries exist post-merge
            return articles.size() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Verifies restore functionality in remove-all mode.
     */
    private static boolean testRestoreRemoveAllMode() {
        try {
            BackupService backupService = new BackupService();
            backupService.restore("group1_backup.json", false); // Restore in remove-all mode

            // Fetch articles to confirm only backup data remains
            ViewHelpArticlesPage viewPage = new ViewHelpArticlesPage(null, "Admin");
            List<HelpArticle> articles = viewPage.fetchHelpArticlesByGroup("Group1");

            // Validate entries match backup contents
            return articles.size() == backupService.getBackupFileArticleCount("group1_backup.json");
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Verifies functionality of saving a help article.
     */
    private static boolean testSaveHelpArticleFunctionality() {
        try {
            CreateHelpArticlePage createPage = new CreateHelpArticlePage(null);
            createPage.saveHelpArticle("Test Article", "Test Body", "Test Keywords", "Test Group", "Test Description", "Beginner", "Test Links");

            // Check if article was saved correctly (implement a method to verify)
            ViewHelpArticlesPage viewPage = new ViewHelpArticlesPage(null, "Admin");
            List<HelpArticle> articles = viewPage.fetchHelpArticlesByGroup("Test Group");
            return articles.stream().anyMatch(a -> a.getTitle().equals("Test Article"));
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
