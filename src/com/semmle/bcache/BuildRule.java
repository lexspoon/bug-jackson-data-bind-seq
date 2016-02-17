package com.semmle.bcache;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.semmle.bcache.util.Seq;

/**
 * An individual build rule. It describes a way to run a command across input
 * artifacts to produce some output files.
 */
public class BuildRule {
	private final Seq<String> commands;
	private final Seq<Artifact> inputs;
	private final BuildRuleName name;
	private final Seq<String> outputs;
	private final Seq<Artifact> rundeps;

	@JsonCreator
	public BuildRule(@JsonProperty("commands") Seq<String> commands,
			@JsonProperty("inputs") Seq<Artifact> inputs, @JsonProperty("name") BuildRuleName name,
			@JsonProperty("outputs") Seq<String> outputs,
			@JsonProperty("rundeps") Seq<Artifact> rundeps) {
		if (inputs == null) {
			inputs = Seq.empty();
		}
		if (name == null) {
			throw new IllegalArgumentException("A rule must have a name");
		}
		if (outputs == null) {
			throw new IllegalArgumentException(name + ": No outputs provided");
		}
		if (outputs.isEmpty()) {
			throw new IllegalArgumentException(name
					+ ": A build rule must produce at least one output");
		}
		if (commands == null) {
			throw new IllegalArgumentException(name + ": No commands provided");
		}
		if (rundeps == null) {
			rundeps = Seq.empty();
		}

		this.commands = commands;
		this.inputs = inputs;
		this.name = name;
		this.outputs = outputs;
		this.rundeps = rundeps;
	}

	/**
	 * The shell command to run to implement the build. The command will be run
	 * in a directory with the inputs copied into it, and it is expected to drop
	 * the output files into that same directory.
	 */
	public Seq<String> commands() {
		return commands;
	}

	/**
	 * The artifacts this build rule depends on.
	 */
	public Seq<Artifact> inputs() {
		return inputs;
	}

	public BuildRuleName name() {
		return name;
	}

	/**
	 * The output files this rule creates.
	 */
	public Seq<String> outputs() {
		return outputs;
	}

	/**
	 * Artifacts that are not necessary to build this rule, but are needed to
	 * make use the output of the rules.
	 */
	public Seq<Artifact> rundeps() {
		return rundeps;
	}

	@Override
	public String toString() {
		return name().name();
	}

	public BuildRule withInputs(Seq<Artifact> inputs) {
		return new BuildRule(commands, inputs, name, outputs, rundeps);
	}
}
