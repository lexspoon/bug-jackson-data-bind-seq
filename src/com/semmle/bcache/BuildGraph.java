package com.semmle.bcache;

import java.io.File;
import java.io.IOException;
import java.util.LinkedHashMap;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.semmle.bcache.util.ResourceError;
import com.semmle.bcache.util.Seq;

/**
 * A collection of build rules. These rules are conventionally stored in a file
 * named <code>brules.json</code>.
 */
public class BuildGraph {
	private static final ObjectMapper jsonMapper = new ObjectMapper();

	public static BuildGraph load(File rulesFile) {
		try {
			return jsonMapper.readValue(rulesFile, BuildGraph.class);
		} catch (JsonParseException e) {
			throw new IllegalArgumentException(e.getMessage());
		} catch (JsonMappingException e) {
			if (e.getCause() != null) {
				throw new IllegalArgumentException(e.getCause().getMessage());
			} else {
				throw new IllegalArgumentException(e.getMessage());
			}
		} catch (IOException e) {
			throw new ResourceError(e);
		}
	}

	/**
	 * Environment variables that should be passed through when running build
	 * commands. These are included in the input hash for a command; changing
	 * any of these will trigger rebuilds.
	 */
	private final Seq<String> environ;

	/**
	 * Environment variables that should be passed through for local workers
	 * only. These are not included in the input hash for a command.
	 */
	private final Seq<String> localenv;

	private LinkedHashMap<String, BuildRuleName> outputToRule;

	private final LinkedHashMap<BuildRuleName, BuildRule> rules;

	@JsonCreator
	public BuildGraph(@JsonProperty("environ") Seq<String> environ,
			@JsonProperty("localenv") Seq<String> localenv,
			@JsonProperty("rules") Seq<BuildRule> rules) {
		if (environ == null) {
			environ = Seq.empty();
		}
		this.environ = environ;
		if (localenv == null) {
			localenv = Seq.empty();
		}
		this.localenv = localenv;
		if (rules == null) {
			throw new IllegalArgumentException("Build graph with no rules");
		}

		this.rules = new LinkedHashMap<BuildRuleName, BuildRule>(rules.size());
		for (BuildRule rule : rules) {
			if (this.rules.put(rule.name(), rule) != null) {
				throw new IllegalArgumentException("Multiple rules named " + rule.name());
			}
		}
	}

	public Seq<String> localenv() {
		return localenv;
	}

	public int numRules() {
		return rules.size();
	}

	@Override
	public String toString() {
		return "BuildGraph with " + rules.size() + " rules";
	}
}
