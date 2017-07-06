/**
 * Odoo, Open Source Management Solution
 * Copyright (C) 2012-today Odoo SA (<http:www.odoo.com>)
 * <p>
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details
 * <p>
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http:www.gnu.org/licenses/>
 * <p>
 * Created on 31/12/14 6:43 PM
 */
package com.odoo.addons.technic.models;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.odoo.base.addons.res.ResUsers;
import com.odoo.core.orm.ODataRow;
import com.odoo.core.orm.OModel;
import com.odoo.core.orm.fields.OColumn;
import com.odoo.core.orm.fields.types.OInteger;
import com.odoo.core.orm.fields.types.OVarchar;
import com.odoo.core.support.OUser;
import com.odoo.core.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class ProjectProject extends OModel {


    OColumn name = new OColumn("Name", OVarchar.class);
    OColumn members = new OColumn("Project members", ResUsers.class, OColumn.RelationType.ManyToMany);

    public ProjectProject(Context context, OUser user) {
        super(context, "project.project", user);
    }

    public List<Integer> selectManyToManyServerIds(String column_name, int row_id) {
        OColumn column = getColumn(column_name);
        OModel rel_model = createInstance(column.getType());
        String table = column.getRelTableName() != null ? column.getRelTableName() : getTableName() + "_" + rel_model.getTableName() + "_rel";
        String base_column = column.getRelBaseColumn() != null ? column.getRelBaseColumn() : getTableName() + "_id";
        String rel_column = column.getRelRelationColumn() != null ? column.getRelRelationColumn() : rel_model.getTableName() + "_id";
        String base_table = getTableName();
        // Getting relation table ids
        List<Integer> ids = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cr = null;
        Cursor crr = null;
        String userId = "0";

        List<ODataRow> rows = rel_model.select(new String[]{"_id"}, "id = ?", new String[]{row_id + ""});
        for (ODataRow row : rows) {
            userId = row.getString("_id");
        }
        try {
            cr = db.query(table, new String[]{base_column}, rel_column + "=?", new String[]{userId}, null, null, null);
            if (cr.moveToFirst()) {
                do {
                    crr = db.query(base_table, new String[]{"id"}, "_id" + "=?", new String[]{cr.getInt(0) + ""}, null, null, null);
                    if (crr.moveToFirst()) {
                        do {
                            ids.add(crr.getInt(0));
                        } while (crr.moveToNext());
                    }
                } while (cr.moveToNext());
            }
        } finally {
            if (cr != null) {
                cr.close();
            }
            if (crr != null) {
                crr.close();
            }
        }
        rel_model.close();
        return ids;
    }

}
