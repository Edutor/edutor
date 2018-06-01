package dk.edutor.eduport

abstract class Solution(
    val id: Int,
    val challenge: Challenge,
    val solver: Person
    )

class StringSolution(
    id: Int,
    challenge: StringChallenge,
    solver: Person,
    val answer: String
    ) : Solution(id, challenge, solver)

class MCSolution(
    id: Int,
    challenge: MCChallenge,
    solver: Person,
    val answers:List<String>
    ) : Solution(id, challenge, solver)

/* Java:
  public class StringSolution extends Solution {
    private final String answer;

    StringSolution(String answer, Person solver, long id) {
      super(solver, id);
      this.answer = answer;
      }

    public String getAnswer() { return answer; }
    }
 */

