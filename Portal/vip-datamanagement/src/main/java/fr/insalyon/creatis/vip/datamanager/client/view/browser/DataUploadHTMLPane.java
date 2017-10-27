/*
Copyright 2009-2015

CREATIS
CNRS UMR 5220 -- INSERM U1044 -- Université Lyon 1 -- INSA Lyon

Authors

Nouha Boujelben (nouha.boujelben@creatis.insa-lyon.fr)
Frédéric Cervenansky (frederic.cervnansky@creatis.insa-lyon.fr)
Rafael Ferreira da Silva (rafael.silva@creatis.insa-lyon.fr)
Tristan Glatard (tristan.glatard@creatis.insa-lyon.fr)
Ibrahim  Kallel (ibrahim.kallel@creatis.insa-lyon.fr)
Kévin Moulin (kevmoulin@wanadoo.fr)
Sorina Pop (sorina.pop@creatis.insa-lyon.fr)

This software is a web portal for pipeline execution on distributed systems.

This software is governed by the CeCILL-B license under French law and
abiding by the rules of distribution of free software.  You can use,
modify and/ or redistribute the software under the terms of the
CeCILL-B license as circulated by CEA, CNRS and INRIA at the following
URL "http://www.cecill.info".

As a counterpart to the access to the source code and rights to copy,
modify and redistribute granted by the license, users are provided
only with a limited warranty and the software's author, the holder of
the economic rights, and the successive licensors have only limited
liability.

In this respect, the user's attention is drawn to the risks associated
with loading, using, modifying and/or developing or reproducing the
software by the user in light of its specific status of free software,
that may mean that it is complicated to manipulate, and that also
therefore means that it is reserved for developers and experienced
professionals having in-depth computer knowledge. Users are therefore
encouraged to load and test the software's suitability as regards
their requirements in conditions enabling the security of their
systems and/or data to be ensured and, more generally, to use and
operate it in the same conditions as regards security.

The fact that you are presently reading this means that you have had
knowledge of the CeCILL-B license and that you accept its terms.
 */
package fr.insalyon.creatis.vip.datamanager.client.view.browser;

import com.google.gwt.core.client.Callback;
import com.google.gwt.core.client.ScriptInjector;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.Window;
import com.smartgwt.client.widgets.HTMLPane;
import fr.insalyon.creatis.vip.core.client.view.CoreConstants;

/**
 *
 * @author camarasu
 */
public class DataUploadHTMLPane extends HTMLPane {

    public DataUploadHTMLPane(String title, String code, 
            String path, String target, boolean usePool) {

        this.setWidth100();
        this.setHeight100();
        this.setShowEdges(true);
     
        this.setContents(
                "<html>"
                + "<head>"
                + "<meta charset=\"UTF-8\">"
                + "<title>" + title + "</title>"
                + "</head>"
                + "<body>"
                + "<form>\n"
                + "<div>\n"
                + "  <label for=\"folder_upload\">Choose folder for upload</label>\n"
                + "  <input type=\"file\" id=\"folder_upload\" name=\"folder_upload\" webkitdirectory  multiple onchange=\"previewFiles(this.files)\">\n"
                + "  <label for=\"files_upload\">Choose files for upload</label>\n"
                + "  <input type=\"file\" id=\"files_upload\" name=\"files_upload\" multiple=\"multiple\" onchange=\"previewFiles(this.files)\">\n"
                + "</div>\n"
                + "<div class=\"preview\">\n"
                + "  <p>No files currently selected for upload</p>\n"
                + "</div>\n"
                + "<div>\n"
                + "  <input type=\"button\" onclick=\"zipAndUploadFiles(\'" + code + "\', \'" + path + "\', \'" + target  + "\', \'" + usePool + "\')\" value=\"Upload\"/>\n"
                + "</div>\n"
                + "</form>\n"
                + "\n"
                + "<div id=\"fileInfo\">\n"
                + "    <div id=\"fileName\"></div>\n"
                + "    <div id=\"fileSize\"></div>\n"
                + "    <div id=\"fileType\"></div>\n"
                + "</div>\n"
                + "<div id=\"progressNumber\"></div>\n"
                + "<pre id=\"serverResponse\"></pre>\n"
                + "\n"
                + "</body>"
                + "</html>");
    }
    
    
}
