package carnegielearning.main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import carnegielearning.model.Problem;
import carnegielearning.model.Skill;
import carnegielearning.model.Student;

/**
 * Engine has the method to return the next problems based on a list of problems
 * and a student.
 * 
 * 
 * @author rianddy
 *
 */
public class Engine {

	/**
	 * This method will return an array list of Problem by calculating the
	 * problem priority. Priority calculation is based on the number of skills
	 * that student's score is below 95.
	 * 
	 * @param student
	 * @param problems
	 * @return
	 */
	public ArrayList<Problem> getNextProblems(Student student,
			ArrayList<Problem> problems) {
		HashMap<Integer, ArrayList<Problem>> priorityProblemMap = new HashMap<Integer, ArrayList<Problem>>();
		HashMap<Skill, Integer> curStuSkillMap = student.getSkillMap();
		int maxPriority = 0;
		for (Problem curPro : problems) {
			int curPriority = 0;
			HashSet<Skill> skillSet = curPro.getSkillSet();
			for (Skill curSkill : skillSet) {
				if (curStuSkillMap.containsKey(curSkill)
						&& curStuSkillMap.get(curSkill) < 95)
					curPriority += 1;
			}
			maxPriority = Math.max(maxPriority, curPriority);
			if (priorityProblemMap.containsKey(curPriority)) {
				priorityProblemMap.get(curPriority).add(curPro);
			} else {
				ArrayList<Problem> temp = new ArrayList<Problem>();
				temp.add(curPro);
				priorityProblemMap.put(curPriority, temp);
			}
		}
		return priorityProblemMap.get(maxPriority);
	}
}
