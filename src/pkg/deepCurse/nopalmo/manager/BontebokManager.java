package pkg.deepCurse.nopalmo.manager;

import java.util.List;

import pkg.deepCurse.bontebok.core.BontebokFunctionInterface;
import pkg.deepCurse.bontebok.core.BontebokInterpreter;
import pkg.deepCurse.nopalmo.global.Reactions;

public class BontebokManager {

	public static void init() {
		BontebokInterpreter.functionMap.put("reply", new BontebokNopalmoFunctionInterface() {

			@Override
			public void run(List<String> args, CommandBlob blob) {
				StringBuilder sB = new StringBuilder();
				for (String i : args) {
					sB.append(i);
				}
				blob.getMessage().reply(sB.toString()).queue();
			}

			@Override
			public int getRequiredArgs() {
				return -1;
			}

		});

		BontebokInterpreter.functionMap.put("react", new BontebokNopalmoFunctionInterface() {

			@Override
			public void run(List<String> args, CommandBlob blob) {
				try {
					blob.getMessage().addReaction(Reactions.getReaction(args.get(0))).queue();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			@Override
			public int getRequiredArgs() {
				return 1;
			}
		});

	}

	public interface BontebokNopalmoFunctionInterface extends BontebokFunctionInterface {

		@Override
		public default void run(List<String> args) {

		}

		void run(List<String> args, CommandBlob blob);
	}

}
