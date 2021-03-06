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
package com.odoo.addons.scrapTire.models;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.odoo.BuildConfig;
import com.odoo.addons.technic.models.TechnicsModel;
import com.odoo.core.orm.OModel;
import com.odoo.core.orm.OValues;
import com.odoo.core.orm.annotation.Odoo;
import com.odoo.core.orm.fields.OColumn;
import com.odoo.core.orm.fields.types.OBoolean;
import com.odoo.core.orm.fields.types.ODateTime;
import com.odoo.core.orm.fields.types.OSelection;
import com.odoo.core.orm.fields.types.OVarchar;
import com.odoo.core.support.OUser;

import java.util.ArrayList;
import java.util.List;

import static android.R.attr.value;

public class ScrapTires extends OModel {
    public static final String AUTHORITY = BuildConfig.APPLICATION_ID + ".core.provider.content.sync.scrap_tire";

    OColumn origin = new OColumn("Актын дугаар", OVarchar.class);
    OColumn technic_id = new OColumn("Техник", TechnicsModel.class, OColumn.RelationType.ManyToOne);
    OColumn tire_ids = new OColumn("Дугуй", TechnicTire.class, OColumn.RelationType.ManyToMany);
    OColumn is_payable = new OColumn("Төлбөртэй эсэх", OBoolean.class);
    OColumn date = new OColumn("Огноо", ODateTime.class);
    OColumn description = new OColumn("Тайлбар", OVarchar.class).setRequired();
    OColumn state = new OColumn("Төлөв", OSelection.class)
            .addSelection("request", "Хүсэлт")
            .addSelection("waiting_approval", "Баталгаа хүлээгдсэн")
            .addSelection("approved", "Батлагдсан")
            .addSelection("refused", "Татгалзсан")
            .addSelection("done", "Дууссан")
            .setDefaultValue("request");

    OColumn tire_photos = new OColumn("Parts photos", TireScrapPhoto.class, OColumn.RelationType.OneToMany).setRelatedColumn("scrap_id");
    @Odoo.Functional(store = true, depends = {"technic_id"}, method = "storeTechnicName")
    OColumn technic_name = new OColumn("Техник", OVarchar.class).setLocalColumn();

    public ScrapTires(Context context, OUser user) {
        super(context, "tire.scrap", user);
    }

    public String storeTechnicName(OValues value) {
        try {
            if (!value.getString("technic_id").equals("false")) {
                List<Object> technic = (ArrayList<Object>) value.get("technic_id");
                return technic.get(1) + "";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

}
