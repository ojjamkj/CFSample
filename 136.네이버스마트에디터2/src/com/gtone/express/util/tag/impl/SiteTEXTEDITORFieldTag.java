package com.gtone.express.util.tag.impl;

import com.gtone.express.server.config.ServerConfig;
import com.gtone.express.util.LocaleHelper;
import com.gtone.express.util.tag.AbstractFieldTag;
import com.gtone.express.util.tag.WebColumnDisplay;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang.StringEscapeUtils;



public class SiteTEXTEDITORFieldTag extends AbstractFieldTag {
   boolean escapeHtml = "Y".equals(ServerConfig.getInstance().getProperty("FieldTagClass.texteditor.escapeHtml"));

   public String getInput(WebColumnDisplay field) throws Exception {
      StringBuffer sb = new StringBuffer();
      sb.append("<div><textarea id='");
      sb.append(field.getCol().getInputParamName()).append("' ");
      if ("N".equals(field.getCol().getNullYn())) {
         sb.append(this.getRequiredStr());
      }
      if ("Y".equals(field.getCol().getEcryptYn())) {
         sb.append(" encrypt=true ");
      }
      if (field.getCol().getSize() > 0) {
         sb.append(" maxLength='").append(field.getCol().getSize()).append("' ");
      }
      sb.append(" name='");
      sb.append(field.getCol().getInputParamName()).append("' class='gt_se2'>").append(StringEscapeUtils.unescapeHtml((String)field.getCol().getDefaultValue())).append("</textarea>");
      sb.append("<script>");
      sb.append("const ").append(field.getCol().getInputParamName()).append("_oEditors = [];");
	  sb.append("nhn.husky.EZCreator.createInIFrame({");
	  sb.append("	oAppRef: ").append(field.getCol().getInputParamName()).append("_oEditors,");
	  sb.append("	elPlaceHolder: '").append(field.getCol().getInputParamName()).append("',");
	  sb.append("	sSkinURI: '/js/se2/SmartEditor2Skin.html',");
	  sb.append("	fOnBeforeUnload : function() {},");
	  sb.append("	htParams : {");
	  sb.append("		bUseToolbar : true,	");
	  sb.append("		bUseVerticalResizer : true,");
	  sb.append("		bUseModeChanger : false,");
	  sb.append("	},");
	  sb.append("	fCreator: 'createSEditor2'");
	  sb.append("});");
      sb.append("</script>");
      sb.append("</div>");

      return sb.toString();
   }

   public Object getOutValue(WebColumnDisplay field) throws Exception {
      String val = (String)this.getValue(field);
      if (this.escapeHtml) {
         val = StringEscapeUtils.unescapeHtml(val);
      }

      val = val.replaceAll("<[Ss][Cc][Rr][Ii][Pp][Tt]", "");
      return val;
   }
}