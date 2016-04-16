package com.hzih.sslvpn.web.taglib;

import cn.collin.commons.domain.PageResult;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;
import java.io.IOException;

/**
 * 分页标签，支持系统中所有的分页栏
 * 
 * @author collin.code@gmail.com
 * 
 */
public class PageBarTag extends TagSupport {
	PageResult ps;

	String baseURL;

	String type;

	String function;

	@Override
	public int doStartTag() throws JspException {
		return SKIP_BODY;
	}

	@Override
	public int doEndTag() throws JspException {
		String html;
		if (type.equalsIgnoreCase("one")) {
			html = barOne();
		} else if (type.equalsIgnoreCase("two")) {
			html = barTwo();
		} else if (type.equalsIgnoreCase("four")) {
			html = barFour();
		} else if (type.equalsIgnoreCase("five")) {
			html = barFive();
		} else {
			html = "not be implemented";
		}
		try {
			this.pageContext.getOut().write(html.toString());
		} catch (IOException e) {
           e.printStackTrace();
		}
		return EVAL_PAGE;
	}

	private String barFour() {
		StringBuilder html = new StringBuilder();
		if (ps == null)
			return html.toString();
		int allResultsAmount = ps.getAllResultsAmount();
		if (allResultsAmount > 0) {
			StringBuffer sbHrefPrefix = new StringBuffer();
			sbHrefPrefix = sbHrefPrefix.append("<a href='").append(baseURL);

			int pageIndex = ps.getPageIndex();
			int pageAmount = ps.getPagesAmount();

			String totalRecordStr = (allResultsAmount > 1) ? "Total records: "
					: "Total record: ";
			html.append("<div style='float:left;'>").append(totalRecordStr)
					.append(ps.getAllResultsAmount()).append("</div>");
			html.append("<div style='float:right;'>Page:").append(pageIndex)
					.append("/").append(pageAmount).append("  ");
			if (pageIndex > 1) {
				html.append(sbHrefPrefix).append(1).append("'>First</a> ");
				html.append(sbHrefPrefix).append(pageIndex - 1).append(
						"'>Prev</a> ");
			}
			if (pageIndex <= 6) {
				int length;
				if (pageAmount >= pageIndex + 5) {
					length = 5 + pageIndex;
				} else {
					length = 1 + pageAmount;
				}
				for (int i = 1; i < length; i++) {
					if (i == pageIndex) {
						html
								.append(
										"<span class='current' style='margin-right:4px;'>")
								.append(i).append("</span>");
					} else {
						html.append(sbHrefPrefix).append(i).append("'>")
								.append(i).append("</a> ");
					}
				}
			} else if (pageIndex > 6 && pageIndex < (pageAmount - 5)) {
				for (int i = pageIndex - 5; i < pageIndex + 5; i++) {
					if (i == pageIndex) {
						html
								.append(
										"<span class='current' style='margin-right:4px;'>")
								.append(i).append("</span>");
					} else {
						html.append(sbHrefPrefix).append(i).append("'>")
								.append(i).append("</a> ");
					}
				}
			} else {
				for (int i = pageIndex - 5; (i <= pageAmount && i < pageIndex + 5); i++) {
					if (i == pageIndex) {
						html
								.append(
										"<span class='current' style='margin-right:4px;'>")
								.append(i).append("</span>");
					} else {
						html.append(sbHrefPrefix).append(i).append("'>")
								.append(i).append("</a> ");
					}
				}
			}
			if (pageIndex < pageAmount) {
				html.append(sbHrefPrefix).append(pageIndex + 1).append(
						"'>Next</a> ");
				html.append(sbHrefPrefix).append(pageAmount).append(
						"'>Last</a>");
			}
			html.append("</div>");
		}
		return html.toString();
	}

	private String barTwo() {
		StringBuilder html = new StringBuilder();
		if (ps == null)
			return html.toString();
		int allResultsAmount = ps.getAllResultsAmount();

		if (allResultsAmount > 0) {
			int pageIndex = ps.getPageIndex();
			int pageAmount = ps.getPagesAmount();

			if (pageIndex > 1) {
				html.append("<span onclick='").append(function).append("(")
						.append(1).append(")'>First</span>");
				html.append("<span onclick='").append(function).append("(")
						.append(ps.getPrevIndex()).append(")'>Prev</span>");
			}

			if (pageIndex <= 6) {
				int length;
				if (pageAmount >= pageIndex + 5) {
					length = 5 + pageIndex;
				} else {
					length = 1 + pageAmount;
				}
				for (int i = 1; i < length; i++) {
					html.append("<span ");
					if (i == pageIndex) {
						html.append("class='current' ");
					} else {
						html.append("onclick='").append(function).append("(")
								.append(i).append(")'");
					}
					html.append(">").append(i).append("</span> ");
				}
			} else if (pageIndex > 6 && pageIndex < (pageAmount - 5)) {
				for (int i = pageIndex - 5; i < pageIndex + 5; i++) {
					html.append("<span ");
					if (i == pageIndex) {
						html.append("class='current' ");
					} else {
						html.append("onclick='").append(function).append("(")
								.append(i).append(")'");
					}
					html.append(">").append(i).append("</span> ");
				}
			} else {
				for (int i = pageIndex - 5; (i <= pageAmount && i < pageIndex + 5); i++) {
					html.append("<span ");
					if (i == pageIndex) {
						html.append("class='current' ");
					} else {
						html.append("onclick='").append(function).append("(")
								.append(i).append(")'");
					}
					html.append(">").append(i).append("</span> ");
				}
			}
			if (pageIndex < pageAmount) {
				html.append("<span onclick='").append(function).append("(")
						.append(ps.getNextIndex()).append(")'>Next</span>");
				html.append("<span onclick='").append(function).append("(")
						.append(pageAmount).append(")'>Last</span>");
			}
		}
		return html.toString();
	}

	private static final String ONE_A = "<p style='float:right'><input type='button' value='go' onclick=\"javascript:window.location='";

	private static final String ONE_B = "'+document.getElementById('go_index').value\" class='btn03'/></p>";

	private static final String ONE_C = "<p style='float:right;' class='sp'>共";

	private static final String ONE_D = "项, 第";

	private static final String ONE_E = "页 ";

	private static final String ONE_F = "'>首页</a> ";

	private static final String ONE_G = "'><<</a> ";

	private static final String ONE_H = "'>>></a> ";

	private static final String ONE_I = "'>末页</a> 跳到 <input type='text' id='go_index' name='go_index'   style='width:30px'  /> 页</p>";

	// finished by collin 2008.8.8
	private String barOne() {
		StringBuilder html = new StringBuilder();
		if (ps == null)
			return html.toString();
		int allResultsAmount = ps.getAllResultsAmount();
		if (allResultsAmount > 0) {
			StringBuffer sbHrefPrefix = new StringBuffer();
			sbHrefPrefix = sbHrefPrefix.append("<a href='").append(baseURL);

			int pageIndex = ps.getPageIndex();
			int pageAmount = ps.getPagesAmount();

			html.append(ONE_A).append(baseURL).append(ONE_B).append(ONE_C)
					.append(ps.getAllResultsAmount()).append(ONE_D).append(
							pageIndex).append("/").append(pageAmount).append(
							ONE_E);
			html.append(sbHrefPrefix).append(1).append(ONE_F);
			html.append(sbHrefPrefix).append(ps.getPrevIndex()).append(ONE_G);

			if (pageIndex <= 6) {
				int length;
				if (pageAmount >= pageIndex + 5) {
					length = 5 + pageIndex;
				} else {
					length = 1 + pageAmount;
				}
				for (int i = 1; i < length; i++) {
					html.append(sbHrefPrefix).append(i).append("'>").append(i)
							.append("</a> ");
				}
			} else if (pageIndex > 6 && pageIndex < (pageAmount - 5)) {
				for (int i = pageIndex - 5; i < pageIndex + 5; i++) {
					html.append(sbHrefPrefix).append(i).append("'>").append(i)
							.append("</a> ");
				}
			} else {
				for (int i = pageIndex - 5; (i <= pageAmount && i < pageIndex + 5); i++) {
					html.append(sbHrefPrefix).append(i).append("'>").append(i)
							.append("</a> ");

				}
			}
			html.append(sbHrefPrefix).append(ps.getNextIndex()).append(ONE_H);
			html.append(sbHrefPrefix).append(pageAmount).append(ONE_I);
		}
		return html.toString();
	}

	// finished by collin 2008.8.8
	private static final String _ONE_A = "<p style='float:right'><input type='button' value='go' onclick=\"javascript:";

	private static final String _ONE_B = "document.getElementById('go_index').value";

	private static final String _ONE_C = "<p style='float:right;' class='sp'>共";

	private static final String _ONE_D = "项, 第";

	private static final String _ONE_E = "页 ";

	private static final String _ONE_F = "'>首页</a> ";

	private static final String _ONE_G = "'><<<B></a> ";

	private static final String _ONE_H = "'></B>>></a> ";

	private static final String _ONE_I = "'>末页</a> 跳到 <input type='text' id='go_index' name='go_index'   style='width:30px'  /> 页</p>";

	private String barFive() {
		StringBuilder html = new StringBuilder();
		if (ps == null)
			return html.toString();
		int allResultsAmount = ps.getAllResultsAmount();
		if (allResultsAmount > 0) {
			StringBuffer sbHrefPrefix = new StringBuffer();
			sbHrefPrefix = sbHrefPrefix.append("<a ").append("");

			int pageIndex = ps.getPageIndex();
			int pageAmount = ps.getPagesAmount();

			html.append(_ONE_A).append(function + "(").append(_ONE_B + ")")
					.append("\"/></p>").append(_ONE_C).append(
							ps.getAllResultsAmount()).append(_ONE_D).append(
							pageIndex).append("/").append(pageAmount).append(
							_ONE_E);
			html.append(sbHrefPrefix).append("href='javascript:").append(
					function).append("(").append("1").append(")")
					.append(_ONE_F);
			html.append(sbHrefPrefix).append("href='javascript:").append(
					function).append("(").append(ps.getPrevIndex()).append(")")
					.append(_ONE_G);

			/*******************************************************************
			 * //这个是改动的代码,当数字到5的时候，会显示出正常效果 if (pageIndex <= 6) { int length; if
			 * (pageAmount >= pageIndex + 1) { length = 0 + pageIndex; } else {
			 * length = 1 + pageAmount; } for (int i = 1; i < length; i++) {
			 * html.append(sbHrefPrefix).append("href='javascript:").append(function).append("(")
			 * .append(i).append(")'>").append(i) .append("</a> "); } } else if
			 * (pageIndex > 6 && pageIndex < (pageAmount - 5)) { for (int i =
			 * pageIndex - 5; i < pageIndex + 0; i++) {
			 * html.append(sbHrefPrefix).append("href='javascript:").append(function).append("(")
			 * .append(i).append(")'>").append(i) .append("</a> "); } } else {
			 * for (int i = pageIndex - 5; (i <= pageAmount && i < pageIndex +
			 * 0); i++) {
			 * html.append(sbHrefPrefix).append("href='javascript:").append(function).append("(")
			 * .append(i).append(")'>").append(i) .append("</a> "); } }
			 ******************************************************************/
			// 取当前的左右各两条,整合
			if (pageAmount < 6 || pageIndex < 4) {
				// 前5项1,2,3,4,5
				for (int i = 1; i < 6; i++) {
					if (i > pageAmount)
						break;
					html.append(sbHrefPrefix).append("href='javascript:")
							.append(function).append("(").append(i).append(
									")'>").append(i).append("</a> ");
				}
			} else if (pageIndex > pageAmount - 2) {
				// 末5项
				for (int i = pageAmount - 4; i <= pageAmount; i++) {
					html.append(sbHrefPrefix).append("href='javascript:")
							.append(function).append("(").append(i).append(
									")'>").append(i).append("</a> ");
				}
			} else {
				// get two at left , self,and two at rigth,to[a,b,X,c,d]
				for (int i = pageIndex - 2; i <= pageIndex + 2; i++) {
					html.append(sbHrefPrefix).append("href='javascript:")
							.append(function).append("(").append(i).append(
									")'>").append(i).append("</a> ");
				}

			}

			html.append(sbHrefPrefix).append("href='javascript:").append(
					function).append("(").append(ps.getNextIndex()).append(")")
					.append(_ONE_H);
			html.append(sbHrefPrefix).append("href='javascript:").append(
					function).append("(").append(pageAmount).append(")")
					.append(_ONE_I);
		}
		return html.toString();
	}

	public void setPs(PageResult ps) {
		this.ps = ps;
	}

	public void setBaseURL(String baseURL) {
		this.baseURL = baseURL;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setFunction(String function) {
		this.function = function;
	}

}
