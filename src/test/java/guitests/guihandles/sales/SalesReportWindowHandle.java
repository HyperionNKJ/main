package guitests.guihandles.sales;

import guitests.GuiRobot;
import guitests.guihandles.StageHandle;
import javafx.stage.Stage;
import seedu.address.model.salesrecord.SalesReport;

/**
 * A handle to the {@code SalesReportWindow} of the application.
 */
public class SalesReportWindowHandle extends StageHandle {

    public static final String SALES_REPORT_WINDOW_TITLE = "Sales Report for %s";

    private final SalesReport salesReport;

    public SalesReportWindowHandle(Stage salesReportWindowStage, SalesReport salesReport) {
        super(salesReportWindowStage);
        this.salesReport = salesReport;
    }

    /**
     * Returns true if a sales report window is currently present in the application.
     */
    public boolean isWindowPresent() {
        String date = salesReport.getDate().toString();
        return new GuiRobot().isWindowShown(String.format(SALES_REPORT_WINDOW_TITLE, date));
    }
}
