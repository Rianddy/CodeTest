package carnegielearning.main;

import java.util.ArrayList;
import java.util.HashMap;

import carnegielearning.model.*;

/**
 * Test case.
 * 
 * @author rianddy
 *
 */
public class Test {

	public static void main(String[] args) {
		HashMap<Skill, Integer> map = new HashMap<Skill, Integer>();
		Skill skill1 = new Skill("add-decimals");
		Skill skill2 = new Skill("multiply-decimals");
		Skill skill3 = new Skill("add-fractions");
		Skill skill4 = new Skill("multiply-fractions");
		map.put(skill3, 97);
		map.put(skill4, 81);
		map.put(skill1, 33);
		map.put(skill2, 47);
		Student student = new Student("Rui", map);

		Problem prob1 = new Problem("prob1");
		prob1.getSkillSet().add(skill1);

		Problem prob2 = new Problem("prob2");
		prob2.getSkillSet().add(skill1);
		prob2.getSkillSet().add(skill2);

		Problem prob3 = new Problem("prob3");
		prob3.getSkillSet().add(skill3);

		Problem prob4 = new Problem("prob4");
		prob4.getSkillSet().add(skill3);
		prob4.getSkillSet().add(skill4);

		Problem prob5 = new Problem("prob5");
		prob5.getSkillSet().add(skill2);
		prob5.getSkillSet().add(skill4);

		Problem prob6 = new Problem("prob6");
		prob6.getSkillSet().add(skill1);
		prob6.getSkillSet().add(skill3);

		ArrayList<Problem> probList = new ArrayList<Problem>();
		probList.add(prob1);
		probList.add(prob2);
		probList.add(prob3);
		probList.add(prob4);
		probList.add(prob5);
		probList.add(prob6);

		Engine engine = new Engine();
		ArrayList<Problem> nextProblems = engine.getNextProblems(student,
				probList);
		for (Problem curPro : nextProblems) {
			System.out.println(curPro.getName());
		}
	}
}
