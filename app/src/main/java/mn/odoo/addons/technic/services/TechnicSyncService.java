package mn.odoo.addons.technic.services;

import android.content.Context;
import android.content.SyncResult;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;

import com.odoo.addons.technic.models.ProjectProject;
import com.odoo.addons.technic.models.TechnicNorm;
import com.odoo.addons.technic.models.TechnicsModel;
import com.odoo.addons.technic.models.UsageUomLine;
import com.odoo.core.orm.ODataRow;
import com.odoo.core.orm.OSQLite;
import com.odoo.core.rpc.helper.ODomain;
import com.odoo.core.service.ISyncFinishListener;
import com.odoo.core.service.OSyncAdapter;
import com.odoo.core.service.OSyncService;
import com.odoo.core.support.OUser;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by baaska on 5/30/17.
 */

public class TechnicSyncService extends OSyncService implements ISyncFinishListener {
    public static final String TAG = TechnicSyncService.class.getSimpleName();

    private OSQLite sqLite = null;

    @Override
    public OSyncAdapter getSyncAdapter(OSyncService service, Context context) {
        return new OSyncAdapter(context, TechnicsModel.class, service, true);
    }

    public SQLiteDatabase getReadableDatabase() {
        return sqLite.getReadableDatabase();
    }

    @Override
    public void performDataSync(OSyncAdapter adapter, Bundle extras, OUser user) {
        if (adapter.getModel().getModelName().equals("technic")) {
            ProjectProject project = new ProjectProject(getApplicationContext(), user);
            project.quickSyncRecords(null);
            Log.i("project======", "111");
//            ODomain domain = new ODomain();
//            List<Integer> projectIds = new ArrayList<>();
//            List<ODataRow> projectRows = project.select();
//            for (ODataRow row : projectRows) {
//                List<ODataRow> memberRows = row.getM2MRecord("members").browseEach();
//                Log.i("memberRows====", memberRows.toString());
//                for (ODataRow memerRow : memberRows) {
//                    if (memerRow.getString("_id").equals(user.getUserId())) {
//                        projectIds.add(row.getInt("id"));
//                    }
//                }
//            }
////            projectIds = project.selectManyToManyServerIds("members", user.getUserId());
//            domain.add("project", "in", projectIds);
//            Log.i("projectIds======", projectIds.toString());
//            Log.i("DOMAIN==========", domain + "");
//            adapter.syncDataLimit(80).setDomain(domain);
            adapter.syncDataLimit(80);
            adapter.onSyncFinish(this);
        }
    }

    @Override
    public OSyncAdapter performNextSync(OUser user, SyncResult syncResult) {
        TechnicNorm norm = new TechnicNorm(getApplicationContext(), user);
        UsageUomLine normLine = new UsageUomLine(getApplicationContext(), user);
        Log.i("next====", "start");
        norm.quickSyncRecords(null);
        normLine.quickSyncRecords(null);
        Log.i("next====", "done");
        return null;
    }
}
