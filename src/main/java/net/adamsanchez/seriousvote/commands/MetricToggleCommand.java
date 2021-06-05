package net.adamsanchez.seriousvote.commands;

import net.adamsanchez.seriousvote.SeriousVote;
import net.adamsanchez.seriousvote.utils.CC;
import net.adamsanchez.seriousvote.utils.CM;
import net.adamsanchez.seriousvote.utils.OutputHelper;
import net.adamsanchez.seriousvote.utils.U;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;

/**
 * Created by Adam Sanchez on 4/15/2018.
 */
public class MetricToggleCommand implements CommandExecutor {
    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {

        src.sendMessage(OutputHelper.strToText("We are temporarily disabling metrics :("));
        /*
        boolean newState = SeriousVote.getInstance().toggleMetrics();
        U.debug(CC.YELLOW + "Metrics Toggled and is now: "
                + (newState ? CC.GREEN + "ON" : CC.RED + "OFF"));
        src.sendMessage(OutputHelper.strToText("&e" + "Metrics Are Now: "
                + (newState ? "&2" + "ON &6- Thank you! If this was an error do /svmetrics again to turn it off." : "&4" + "OFF - Sorry to see you go :( + If this was an error do /svmetrics again to turn it on.")));
         */
        return CommandResult.success();
    }
}