package com.netspace.crm;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Schema;

public class MyDaoGenerator {

    public static void main(String args[]) throws Exception {
        Schema schema = new Schema(1, "taskDB");

        Entity activity = schema.addEntity("Task");
        activity.addStringProperty("id").notNull().primaryKey();
        activity.addStringProperty("title").notNull();
        activity.addStringProperty("description").notNull();
        activity.addDateProperty("startDate").notNull();
        activity.addDateProperty("finishDate").notNull();
        activity.addDateProperty("createdOn").notNull();
        activity.addDateProperty("modifiedOn").notNull();
        activity.addBooleanProperty("syncronized");
        activity.addBooleanProperty("newTask");

        schema.enableKeepSectionsByDefault();

        new DaoGenerator().generateAll(schema, args[0]);
    }
}
