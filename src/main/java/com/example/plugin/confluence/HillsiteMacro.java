package com.example.plugin.confluence;

import java.util.Map;

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
 * Creates a hillsite macro.
 */
public class HillsiteMacro extends BaseMacro implements Macro {

	private final String BOX_TEMPLATE = "vm/macro-hillsite.vm";

	@SuppressWarnings("rawtypes")
	@Override
	public String execute(final Map params, final String body, final RenderContext renderContext) throws MacroException {
		VelocityContext contextMap = new VelocityContext(MacroUtils.defaultVelocityContext());
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

	@Override
	public BodyType getBodyType() {
		return BodyType.NONE;
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
		return false;
	}

	@Override
	public RenderMode getBodyRenderMode() {
		return RenderMode.ALL;
	}

	@Override
	public boolean suppressMacroRenderingDuringWysiwyg() {
		return true;
	}

	@Override
	public boolean suppressSurroundingTagDuringWysiwygRendering() {
		return false;
	}
}
