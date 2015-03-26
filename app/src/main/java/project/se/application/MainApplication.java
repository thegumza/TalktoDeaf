package project.se.application;

import android.app.Application;

import org.acra.ACRA;
import org.acra.annotation.ReportsCrashes;
import org.acra.sender.HttpSender;

/**
 * Created by BKK on 26/3/2558.
 */
@ReportsCrashes(
        httpMethod = HttpSender.Method.PUT,
        reportType = HttpSender.Type.JSON,
        formUri = "http://bkkkjst.iriscouch.com/acra-talktodeaf/_design/acra-storage/_update/report",
        formUriBasicAuthLogin = "test",
        formUriBasicAuthPassword = "12345"
)
public class MainApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        ACRA.init(this);
    }
}
