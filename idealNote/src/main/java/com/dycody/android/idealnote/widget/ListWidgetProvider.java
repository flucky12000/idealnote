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
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.SparseArray;
import android.widget.RemoteViews;

import com.dycody.android.idealnote.MainActivity;
import com.dycody.android.idealnote.utils.Constants;


public class ListWidgetProvider extends WidgetProvider {

    @Override
    protected RemoteViews getRemoteViews(Context mContext, int widgetId,
                                         boolean isSmall, boolean isSingleLine, 
                                         SparseArray<PendingIntent> pendingIntentsMap) {
        RemoteViews views;
        if (isSmall) {
            views = new RemoteViews(mContext.getPackageName(),
                    com.dycody.android.idealnote.R.layout.widget_layout_small);
            views.setOnClickPendingIntent(com.dycody.android.idealnote.R.id.list,
                    pendingIntentsMap.get(com.dycody.android.idealnote.R.id.list));
        } else if (isSingleLine) {
            views = new RemoteViews(mContext.getPackageName(),
                    com.dycody.android.idealnote.R.layout.widget_layout);
            views.setOnClickPendingIntent(com.dycody.android.idealnote.R.id.add,
                    pendingIntentsMap.get(com.dycody.android.idealnote.R.id.add));
            views.setOnClickPendingIntent(com.dycody.android.idealnote.R.id.list,
                    pendingIntentsMap.get(com.dycody.android.idealnote.R.id.list));
            views.setOnClickPendingIntent(com.dycody.android.idealnote.R.id.camera,
                    pendingIntentsMap.get(com.dycody.android.idealnote.R.id.camera));
        } else {
            views = new RemoteViews(mContext.getPackageName(),
                    com.dycody.android.idealnote.R.layout.widget_layout_list);
            views.setOnClickPendingIntent(com.dycody.android.idealnote.R.id.add,
                    pendingIntentsMap.get(com.dycody.android.idealnote.R.id.add));
            views.setOnClickPendingIntent(com.dycody.android.idealnote.R.id.list,
                    pendingIntentsMap.get(com.dycody.android.idealnote.R.id.list));
            views.setOnClickPendingIntent(com.dycody.android.idealnote.R.id.camera,
                    pendingIntentsMap.get(com.dycody.android.idealnote.R.id.camera));

            // Set up the intent that starts the ListViewService, which will
            // provide the views for this collection.
            Intent intent = new Intent(mContext, ListWidgetService.class);
            // Add the app widget ID to the intent extras.
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId);
            intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));

            views.setRemoteAdapter(com.dycody.android.idealnote.R.id.widget_list, intent);

            Intent clickIntent = new Intent(mContext, MainActivity.class);
            clickIntent.setAction(Constants.ACTION_WIDGET);
            PendingIntent clickPI = PendingIntent.getActivity(mContext, 0,
                    clickIntent, PendingIntent.FLAG_UPDATE_CURRENT);

            views.setPendingIntentTemplate(com.dycody.android.idealnote.R.id.widget_list, clickPI);
        }
        return views;
    }

}
