package com.example.plugin.confluence;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.velocity.VelocityContext;

import com.atlassian.confluence.content.render.xhtml.ConversionContext;
import com.atlassian.confluence.content.render.xhtml.DefaultConversionContext;
import com.atlassian.confluence.macro.Macro;
import com.atlassian.confluence.macro.MacroExecutionException;
import com.atlassian.confluence.renderer.radeox.macros.MacroUtils;
import com.atlassian.confluence.util.velocity.VelocityUtils;
import com.atlassian.renderer.RenderContext;
import com.atlassian.renderer.v2.RenderMode;
import com.atlassian.renderer.v2.macro.BaseMacro;
import com.atlassian.renderer.v2.macro.MacroException;

/**
 * Creates a box macro with images. Preferably use section and column macros as
 * containers.
 * 
 * @example {section} {column}
 *          {box:size=small|title=Title}lorem ipsum{box}
 *          {column} {section}
 */
public class BoxMacro extends BaseMacro implements Macro {
	private final Logger logger = Logger.getLogger(BoxMacro.class);

	private final String BOX_TEMPLATE = "vm/macro-box.vm";
	private final Set<String> SIZES = new HashSet<String>(Arrays.asList(new String[] { "small", "medium", "large" }));
	private final String DEFAULT_SIZE = "small";
	private final Set<String> COLORS = new HashSet<String>(Arrays.asList(new String[] { "red", "grey" }));
	private final String DEFAULT_COLOR = "red";

	@SuppressWarnings("rawtypes")
	@Override
	public String execute(final Map params, final String body, final RenderContext renderContext) throws MacroException {
		VelocityContext contextMap = new VelocityContext(MacroUtils.defaultVelocityContext());

		putStringContextVar(contextMap, params, "size", SIZES, DEFAULT_SIZE);
		putStringContextVar(contextMap, params, "color", COLORS, DEFAULT_COLOR);

		// put title into map
		contextMap.put("title", params.get("title"));

		// put parts from body into map
		putBodyPartsIntoMap(body, contextMap);

		// check new tab option
		if ("true".equals(params.get("newTab"))) {
			contextMap.put("newTab", "open_new_window");
		}

		return VelocityUtils.getRenderedTemplate(BOX_TEMPLATE, contextMap);
	}

	@Override
	public String execute(final Map<String, String> params, final String body, final ConversionContext cc)
			throws MacroExecutionException {
		try {
			DefaultConversionContext defaultConversionContext = (DefaultConversionContext) cc;
			RenderContext renderContext = defaultConversionContext.getPageContext();
			return execute(params, body, renderContext);
		}
		catch (MacroException e) {
			throw new MacroExecutionException(e);
		}
	}

	private void putBodyPartsIntoMap(final String body, final VelocityContext contextMap) {
		logger.debug("Raw body: " + body);
		String finalBody = extractImageFromBody(body, contextMap);
		finalBody = extractLinkFromBody(finalBody, contextMap);

		finalBody = StringUtils.trimToEmpty(finalBody);
		logger.debug("New body: " + finalBody);
		contextMap.put("body", finalBody);
	}

	protected String extractLinkFromBody(String body, final VelocityContext contextMap) {
		Matcher linkMatch = Pattern.compile("<a.*?</a>").matcher(body);
		if (linkMatch.find()) {
			final String link = linkMatch.group(0);
			// remove link from body
			body = body.replace(link, "");

			// get href value from link
			Matcher hrefMatch = Pattern.compile("href=\"(.*?)\"").matcher(link);
			if (hrefMatch.find()) {
				contextMap.put("url", hrefMatch.group(1));
			}
		}

		return body;
	}

	protected String extractImageFromBody(String body, final VelocityContext contextMap) {
		Matcher imageMatch = Pattern.compile("<img.*?>").matcher(body);
		if (imageMatch.find()) {
			final String image = imageMatch.group(0);
			// extract image from body
			body = body.replace(image, "");

			// get src value from image
			Matcher srcMatch = Pattern.compile("src=\"(.*?)\"").matcher(image);
			if (srcMatch.find()) {
				contextMap.put("imagesrc", srcMatch.group(1));
			}
		}
		else {
			contextMap.put("image", "");
		}
		return body;
	}

	@Override
	public BodyType getBodyType() {
		return BodyType.RICH_TEXT;
	}

	@Override
	public OutputType getOutputType() {
		return OutputType.BLOCK;
	}

	@Override
	public boolean isInline() {
		return false;
	}

	@Override
	public boolean hasBody() {
		return true;
	}

	@Override
	public RenderMode getBodyRenderMode() {
		return RenderMode.allow(RenderMode.F_LINKS | RenderMode.F_LINEBREAKS | RenderMode.F_MACROS
				| RenderMode.F_MACROS_ERR_MSG | RenderMode.F_PHRASES);
	}

	@Override
	public boolean suppressMacroRenderingDuringWysiwyg() {
		return true;
	}

	@Override
	public boolean suppressSurroundingTagDuringWysiwygRendering() {
		return false;
	}

	/**
	 * Setzt einen Wert aus einem Enum-Type-Parameter, wenn nicht vorhanden wird ein
	 * Default-Wert gesetzt.
	 * 
	 * @param contextMap
	 *          Context
	 * @param params
	 *          Makro-Parameter
	 * @param paramname
	 *          Parametername
	 * @param set
	 *          Enum-Set
	 * @param def
	 *          Default-Wert
	 */
	public static void putStringContextVar(final VelocityContext contextMap, final Map params, final String paramname,
			final Set set, final String def) {
		Object p = params.get(paramname);
		if (set.contains(p)) {
			contextMap.put(paramname, p);
		}
		else {
			contextMap.put(paramname, def);
		}
	}
}
