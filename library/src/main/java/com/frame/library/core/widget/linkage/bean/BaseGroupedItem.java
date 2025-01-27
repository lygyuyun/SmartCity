package com.frame.library.core.widget.linkage.bean;
/*
 * Copyright (c) 2018-2019. KunMinX
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


import java.io.Serializable;

/**
 * items which support grouped
 * <p>
 * Create by KunMinX at 19/4/29
 */
public abstract class BaseGroupedItem<T extends BaseGroupedItem.ItemInfo> implements Serializable {
    public boolean isHeader;
    public T info;
    public String header;

    public BaseGroupedItem(boolean isHeader, String header) {
        this.isHeader = isHeader;
        this.header = header;
        this.info = null;
    }

    public BaseGroupedItem(T info) {
        this.isHeader = false;
        this.header = null;
        this.info = info;
    }

    public static class ItemInfo {
        private String group;
        private String title;
        private String richContent;
        private int id;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public ItemInfo(String title, String group,String richContent) {
            this.title = title;
            this.group = group;
            this.richContent = richContent;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getGroup() {
            return group;
        }

        public void setGroup(String group) {
            this.group = group;
        }

        public String getRichContent() {
            return richContent;
        }

        public void setRichContent(String richContent) {
            this.richContent = richContent;
        }
    }
}
