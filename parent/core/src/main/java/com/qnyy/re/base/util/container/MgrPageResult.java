package com.qnyy.re.base.util.container;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Created by E_Iva on 2017.12.26.0026.
 */
@Getter@Setter
public class MgrPageResult<T> extends PageResult {
    
    private boolean entityOrField = true;
    public MgrPageResult(List result, Integer totalCount, Integer currentPage, Integer pageSize) {
        super(result, totalCount, currentPage, pageSize);
    }

    //拼接分页 页面及JS函数
    public String getPageStr() {
        StringBuffer sb = new StringBuffer();
        if(totalCount>0){
            sb.append("	<ul class=\"pagination pull-right no-margin\">\n");
            if(currentPage==1){
                sb.append("	<li><a>共<font color=red>"+totalCount+"</font>条</a></li>\n");
                sb.append("	<li><input type=\"number\" value=\"\" id=\"toGoPage\" style=\"width:50px;text-align:center;float:left\" placeholder=\"页码\"/></li>\n");
                sb.append("	<li style=\"cursor:pointer;\"><a onclick=\"toTZ();\"  class=\"btn btn-mini btn-success\">跳转</a></li>\n");
                sb.append("	<li><a>首页</a></li>\n");
                sb.append("	<li><a>上页</a></li>\n");
            }else{
                sb.append("	<li><a>共<font color=red>"+totalCount+"</font>条</a></li>\n");
                sb.append("	<li><input type=\"number\" value=\"\" id=\"toGoPage\" style=\"width:50px;text-align:center;float:left\" placeholder=\"页码\"/></li>\n");
                sb.append("	<li style=\"cursor:pointer;\"><a onclick=\"toTZ();\"  class=\"btn btn-mini btn-success\">跳转</a></li>\n");
                sb.append("	<li style=\"cursor:pointer;\"><a onclick=\"nextPage(1)\">首页</a></li>\n");
                sb.append("	<li style=\"cursor:pointer;\"><a onclick=\"nextPage("+prevPage+")\">上页</a></li>\n");
            }
            int showTag = 5;//分页标签显示数量
            int startTag = 1;
            if(currentPage>showTag){
                startTag = currentPage-1;
            }
            int endTag = startTag+showTag-1;
            for(int i=startTag; i<=totalPage && i<=endTag; i++){
                if(currentPage==i)
                    sb.append("<li class=\"active\"><a><font color='white'>"+i+"</font></a></li>\n");
                else
                    sb.append("	<li style=\"cursor:pointer;\"><a onclick=\"nextPage("+i+")\">"+i+"</a></li>\n");
            }
            if(currentPage.equals(totalPage)){
                sb.append("	<li><a>下页</a></li>\n");
                sb.append("	<li><a>尾页</a></li>\n");
            }else{
                sb.append("	<li style=\"cursor:pointer;\"><a onclick=\"nextPage("+nextPage+")\">下页</a></li>\n");
                sb.append("	<li style=\"cursor:pointer;\"><a onclick=\"nextPage("+totalPage+")\">尾页</a></li>\n");
            }
            sb.append("	<li><a>共"+totalPage+"页</a></li>\n");
            sb.append("	<li><select title='显示条数' style=\"width:55px;float:left;margin-top:1px;\" onchange=\"changeCount(this.value)\">\n");
            sb.append("	<option value='"+pageSize+"'>"+pageSize+"</option>\n");
            sb.append("	<option value='10'>10</option>\n");
            sb.append("	<option value='20'>20</option>\n");
            sb.append("	<option value='30'>30</option>\n");
            sb.append("	<option value='40'>40</option>\n");
            sb.append("	<option value='50'>50</option>\n");
            sb.append("	<option value='60'>60</option>\n");
            sb.append("	<option value='70'>70</option>\n");
            sb.append("	<option value='80'>80</option>\n");
            sb.append("	<option value='90'>90</option>\n");
            sb.append("	<option value='99'>99</option>\n");
            sb.append("	</select>\n");
            sb.append("	</li>\n");

            sb.append("</ul>\n");
            sb.append("<script type=\"text/javascript\">\n");

            //换页函数
            sb.append("function nextPage(page){");
            sb.append(" top.jzts();");
            sb.append("	if(true && document.forms[0]){\n");
            sb.append("		var url = document.forms[0].getAttribute(\"action\");\n");
            sb.append("		if(url.indexOf('?')>-1){url += \"&"+(entityOrField?"currentPage":"page.currentPage")+"=\";}\n");
            sb.append("		else{url += \"?"+(entityOrField?"currentPage":"page.currentPage")+"=\";}\n");
            sb.append("		url = url + page + \"&" +(entityOrField?"pageSize":"page.pageSize")+"="+pageSize+"\";\n");
            sb.append("		document.forms[0].action = url;\n");
            sb.append("		document.forms[0].submit();\n");
            sb.append("	}else{\n");
            sb.append("		var url = document.location+'';\n");
            sb.append("		if(url.indexOf('?')>-1){\n");
            sb.append("			if(url.indexOf('currentPage')>-1){\n");
            sb.append("				var reg = /currentPage=\\d*/g;\n");
            sb.append("				url = url.replace(reg,'currentPage=');\n");
            sb.append("			}else{\n");
            sb.append("				url += \"&"+(entityOrField?"currentPage":"page.currentPage")+"=\";\n");
            sb.append("			}\n");
            sb.append("		}else{url += \"?"+(entityOrField?"currentPage":"page.currentPage")+"=\";}\n");
            sb.append("		url = url + page + \"&" +(entityOrField?"pageSize":"page.pageSize")+"="+pageSize+"\";\n");
            sb.append("		document.location = url;\n");
            sb.append("	}\n");
            sb.append("}\n");

            //调整每页显示条数
            sb.append("function changeCount(value){");
            sb.append(" top.jzts();");
            sb.append("	if(true && document.forms[0]){\n");
            sb.append("		var url = document.forms[0].getAttribute(\"action\");\n");
            sb.append("		if(url.indexOf('?')>-1){url += \"&"+(entityOrField?"currentPage":"page.currentPage")+"=\";}\n");
            sb.append("		else{url += \"?"+(entityOrField?"currentPage":"page.currentPage")+"=\";}\n");
            sb.append("		url = url + \"1&" +(entityOrField?"pageSize":"page.pageSize")+"=\"+value;\n");
            sb.append("		document.forms[0].action = url;\n");
            sb.append("		document.forms[0].submit();\n");
            sb.append("	}else{\n");
            sb.append("		var url = document.location+'';\n");
            sb.append("		if(url.indexOf('?')>-1){\n");
            sb.append("			if(url.indexOf('currentPage')>-1){\n");
            sb.append("				var reg = /currentPage=\\d*/g;\n");
            sb.append("				url = url.replace(reg,'currentPage=');\n");
            sb.append("			}else{\n");
            sb.append("				url += \"1&"+(entityOrField?"currentPage":"page.currentPage")+"=\";\n");
            sb.append("			}\n");
            sb.append("		}else{url += \"?"+(entityOrField?"currentPage":"page.currentPage")+"=\";}\n");
            sb.append("		url = url + \"&" +(entityOrField?"pageSize":"page.pageSize")+"=\"+value;\n");
            sb.append("		document.location = url;\n");
            sb.append("	}\n");
            sb.append("}\n");

            //跳转函数 
            sb.append("function toTZ(){");
            sb.append("var toPaggeVlue = document.getElementById(\"toGoPage\").value;");
            sb.append("if(toPaggeVlue == ''){document.getElementById(\"toGoPage\").value=1;return;}");
            sb.append("if(isNaN(Number(toPaggeVlue))){document.getElementById(\"toGoPage\").value=1;return;}");
            sb.append("nextPage(toPaggeVlue);");
            sb.append("}\n");
            sb.append("</script>\n");
        }
        return sb.toString();
    }
}
