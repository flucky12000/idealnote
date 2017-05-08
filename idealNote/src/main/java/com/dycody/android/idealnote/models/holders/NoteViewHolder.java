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

package com.dycody.android.idealnote.models.holders;

import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.neopixl.pixlui.components.textview.TextView;

import com.dycody.android.idealnote.models.views.SquareImageView;


public class NoteViewHolder {

	public NoteViewHolder(View view) {
		ButterKnife.bind(this, view);
	}

	@Bind(com.dycody.android.idealnote.R.id.root) public View root;
	@Bind(com.dycody.android.idealnote.R.id.card_layout) public View cardLayout;
	@Bind(com.dycody.android.idealnote.R.id.category_marker) public View categoryMarker;

	@Bind(com.dycody.android.idealnote.R.id.note_title) public TextView title;
	@Bind(com.dycody.android.idealnote.R.id.note_content) public TextView content;
	@Bind(com.dycody.android.idealnote.R.id.note_date) public TextView date;

	@Bind(com.dycody.android.idealnote.R.id.archivedIcon) public ImageView archiveIcon;
	@Bind(com.dycody.android.idealnote.R.id.locationIcon) public ImageView locationIcon;
	@Bind(com.dycody.android.idealnote.R.id.alarmIcon) public ImageView alarmIcon;
	@Bind(com.dycody.android.idealnote.R.id.lockedIcon) public ImageView lockedIcon;
	@Nullable @Bind(com.dycody.android.idealnote.R.id.attachmentIcon) public ImageView attachmentIcon;
	@Nullable @Bind(com.dycody.android.idealnote.R.id.attachmentThumbnail) public SquareImageView attachmentThumbnail;
}