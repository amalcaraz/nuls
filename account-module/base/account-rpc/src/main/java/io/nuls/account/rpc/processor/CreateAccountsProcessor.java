package io.nuls.account.rpc.processor;

import io.nuls.core.tools.cmd.CommandBuilder;
import io.nuls.core.tools.cmd.CommandHelper;
import io.nuls.core.tools.str.StringUtils;
import io.nuls.kernel.lite.annotation.Cmd;
import io.nuls.kernel.lite.annotation.Component;
import io.nuls.kernel.model.CommandResult;
import io.nuls.kernel.processor.CommandProcessor;

/**
 * @author: Charlie
 * @date: 2018/5/25
 */
@Cmd
@Component
public class CreateAccountsProcessor implements CommandProcessor {
    @Override
    public String getCommand() {
        return "createaccounts";
    }

    @Override
    public String getHelp() {
        CommandBuilder builder = new CommandBuilder();

        builder.newLine(getCommandDescription())
                .newLine("\t<count> The count of accounts you want to create, - Required")
                .newLine("\t[password] The password for the account, the password is between 8 and 20 inclusive of numbers and letters , not encrypted by default");
        return builder.toString();
    }

    @Override
    public String getCommandDescription() {
        return "createaccounts <count> [password] --create <count> accounts , encrypted by [password]";
    }

    @Override
    public boolean argsValidate(String[] args) {
        int length = args.length;
        if (length < 2 || length > 3) {
            return false;
        }
        if (!CommandHelper.checkArgsIsNull(args)) {
            return false;
        }
        if (!StringUtils.isNumeric(args[1])) {
            return false;
        }
        if (length == 3 && !StringUtils.validPassword(args[2])) {
            return false;
        }
        return true;
    }

    @Override
    public CommandResult execute(String[] args) {
        // todo auto-generated method stub
        return null;
    }
}