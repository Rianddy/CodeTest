package carnegielearning.model;

import java.util.HashSet;

/**
 * Create a Problem model. Its member variable is name and skills set.
 * 
 * @author rianddy
 *
 */
public class Problem {
	private String name;
	private HashSet<Skill> skillSet;

	public Problem(String name) {
		this.name = name;
		this.skillSet = new HashSet<Skill>();
	}

	public Problem(String name, HashSet<Skill> skillSet) {
		this.name = name;
		this.skillSet = skillSet;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public HashSet<Skill> getSkillSet() {
		return skillSet;
	}

	public void setSkillSet(HashSet<Skill> skillSet) {
		this.skillSet = skillSet;
	}

}
