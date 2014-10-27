package carnegielearning.model;

import java.util.HashMap;

/**
 * Create a Student model which has name and skills map.
 * 
 * TODO: limit the score between 0-100 when create an instance of Student.
 * 
 * @author rianddy
 *
 */
public class Student {
	private String name;
	private HashMap<Skill, Integer> skillMap = new HashMap<Skill, Integer>();

	public Student(String name, HashMap<Skill, Integer> skillMap) {
		this.name = name;
		this.skillMap = skillMap;
	}

	/**
	 * This method is to add skill to student. If the student already had the
	 * skill. Just take the mid score between new score and the existing score.
	 * 
	 * @param skill
	 * @param score
	 * @return
	 */
	public boolean addScoreToSkill(Skill skill, int score) {
		if (score > 100 || score < 0)
			return false;
		else {
			if (skillMap.containsKey(skill))
				skillMap.put(skill, (score + skillMap.get(skill)) / 2);
			else
				skillMap.put(skill, score);
		}
		return true;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public HashMap<Skill, Integer> getSkillMap() {
		return skillMap;
	}

	public void setSkillMap(HashMap<Skill, Integer> skillMap) {
		this.skillMap = skillMap;
	}

}
