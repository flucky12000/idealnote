/*
 * Copyright (C) 2015 Federico Iosue (federico.iosue@gmail.com)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.dycody.android.idealnote.widget;


import android.app.PendingIntent;
import android.content.Context;
import android.util.SparseArray;
import android.widget.RemoteViews;


public class SimpleWidgetProvider extends WidgetProvider {


    @Override
    protected RemoteViews getRemoteViews(Context mContext, int widgetId, boolean isSmall, boolean isSingleLine, 
                                         SparseArray<PendingIntent> pendingIntentsMap) {
        RemoteViews views;
        if (isSmall) {
            views = new RemoteViews(mContext.getPackageName(), com.dycody.android.idealnote.R.layout.widget_layout_small);
            views.setOnClickPendingIntent(com.dycody.android.idealnote.R.id.list, pendingIntentsMap.get(com.dycody.android.idealnote.R.id.list));
        } else {
            views = new RemoteViews(mContext.getPackageName(), com.dycody.android.idealnote.R.layout.widget_layout);
            views.setOnClickPendingIntent(com.dycody.android.idealnote.R.id.add, pendingIntentsMap.get(com.dycody.android.idealnote.R.id.add));
            views.setOnClickPendingIntent(com.dycody.android.idealnote.R.id.list, pendingIntentsMap.get(com.dycody.android.idealnote.R.id.list));
            views.setOnClickPendingIntent(com.dycody.android.idealnote.R.id.camera, pendingIntentsMap.get(com.dycody.android.idealnote.R.id.camera));
        }
        return views;
    }
}