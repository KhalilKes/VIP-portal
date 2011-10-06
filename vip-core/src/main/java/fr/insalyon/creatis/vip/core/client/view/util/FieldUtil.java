/* Copyright CNRS-CREATIS
 *
 * Rafael Silva
 * rafael.silva@creatis.insa-lyon.fr
 * http://www.rafaelsilva.com
 *
 * This software is a grid-enabled data-driven workflow manager and editor.
 *
 * This software is governed by the CeCILL  license under French law and
 * abiding by the rules of distribution of free software.  You can  use,
 * modify and/ or redistribute the software under the terms of the CeCILL
 * license as circulated by CEA, CNRS and INRIA at the following URL
 * "http://www.cecill.info".
 *
 * As a counterpart to the access to the source code and  rights to copy,
 * modify and redistribute granted by the license, users are provided only
 * with a limited warranty  and the software's author,  the holder of the
 * economic rights,  and the successive licensors  have only  limited
 * liability.
 *
 * In this respect, the user's attention is drawn to the risks associated
 * with loading,  using,  modifying and/or developing or reproducing the
 * software by the user in light of its specific status of free software,
 * that may mean  that it is complicated to manipulate,  and  that  also
 * therefore means  that it is reserved for developers  and  experienced
 * professionals having in-depth computer knowledge. Users are therefore
 * encouraged to load and test the software's suitability as regards their
 * requirements in conditions enabling the security of their systems and/or
 * data to be ensured and,  more generally, to use and operate it in the
 * same conditions as regards security.
 *
 * The fact that you are presently reading this means that you have had
 * knowledge of the CeCILL license and that you accept its terms.
 */
package fr.insalyon.creatis.vip.core.client.view.util;

import com.google.gwt.i18n.client.NumberFormat;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.DateDisplayFormat;
import com.smartgwt.client.types.ListGridFieldType;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.FormItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.form.fields.events.ChangedEvent;
import com.smartgwt.client.widgets.form.fields.events.ChangedHandler;
import com.smartgwt.client.widgets.grid.CellFormatter;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;

/**
 *
 * @author Rafael Silva
 */
public class FieldUtil {

    /**
     * Gets a ListGridField configured to display an icon.
     * 
     * @param name Field name
     * @return List grid field
     */
    public static ListGridField getIconGridField(String name) {
        ListGridField iconField = new ListGridField(name, " ", 30);
        iconField.setAlign(Alignment.CENTER);
        iconField.setType(ListGridFieldType.IMAGE);
        iconField.setImageURLSuffix(".png");
        iconField.setImageWidth(12);
        iconField.setImageHeight(12);
        return iconField;
    }

    /**
     * Gets a ListGridField configured to display dates.
     * 
     * @return List grid field
     */
    public static ListGridField getDateField() {
        ListGridField dateField = new ListGridField("date", "Date", 120);
        dateField.setType(ListGridFieldType.DATE);
        dateField.setDateFormatter(DateDisplayFormat.TOUSSHORTDATETIME);
        return dateField;
    }

    /**
     * Gets a DynamicForm with the specified list of items set.
     * 
     * @param items List of form items
     * @return 
     */
    public static DynamicForm getForm(FormItem... items) {
        DynamicForm form = new DynamicForm();
        form.setFields(items);
        return form;
    }

    /**
     * Gets a TextItem configured according to the provided parameters.
     * 
     * @param size Field size
     * @param showTitle If title should be displayed
     * @param title Title to be displayed
     * @param keyPressFilter Regular expression filter
     * @return 
     */
    public static TextItem getTextItem(int size, boolean showTitle, String title,
            String keyPressFilter) {

        TextItem textItem = new TextItem();
        textItem.setTitle(title);
        textItem.setShowTitle(showTitle);
        textItem.setWidth(size);
        textItem.setKeyPressFilter(keyPressFilter);
        textItem.setAlign(Alignment.LEFT);
        textItem.setRequired(true);
        textItem.addChangedHandler(new ChangedHandler() {

            public void onChanged(ChangedEvent event) {
                event.getItem().validate();
            }
        });

        return textItem;
    }

    /**
     * Gets a CellFormatter to parse file sizes.
     * 
     * @return 
     */
    public static CellFormatter getSizeCellFormatter() {

        return new CellFormatter() {

            public String format(Object value, ListGridRecord record, int rowNum, int colNum) {

                if (value == null) {
                    return null;
                }

                long length = ((Number) value).longValue();
                if (length > 0) {
                    String size = length + " B";
                    NumberFormat nf = NumberFormat.getFormat("#.##");
                    if (length / 1024 > 0) {
                        if (length / (1024 * 1024) > 0) {
                            if (length / (1024 * 1024 * 1024) > 0) {
                                size = nf.format(length / (double) (1024 * 1024 * 1024)) + " GB";
                            } else {
                                size = nf.format(length / (double) (1024 * 1024)) + " MB";
                            }
                        } else {
                            size = nf.format(length / (double) 1024) + " KB";
                        }
                    }
                    return size;
                
                } else {
                    return "";
                }
            }
        };
    }
}