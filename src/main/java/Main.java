import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;
import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.message.Message;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.event.message.MessageCreateEvent;
import org.javacord.api.listener.message.MessageCreateListener;

import javax.swing.Timer;

import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class Main {

    String prefix, token;
    DiscordApi api;

    public Main()
    {
        prefix = ";";
        token = ""; //TODO: your token goes here (I'm not uploading mine lol)
        api = new DiscordApiBuilder().setToken(token).login().join();
    }

    public static void main(String[] args)
    {
        Main myBot = new Main();
        myBot.run();

    }

    public void run() {
        api.addListener(new Listener());
    }
}

class Listener implements MessageCreateListener
{
    final String prefix = ";";
    Message msg;
    TextChannel ch;
    int lmaoCount = 0;

    @Override
    public void onMessageCreate(MessageCreateEvent event) {
        if (event.getMessageAuthor().isRegularUser()) //if not a bot
        {
            if (event.getMessageContent().startsWith(prefix))
            {
                msg = event.getMessage();
                ch = event.getChannel();
                doStuff(event);
            }

            int number = (int)(Math.random()*100+1);
            if (number < 3) //3% chance of happening
            {
                event.getMessage().reply("Lmao go do homework instead of wasting your time :laughing: ");
            }

            if(event.getMessageContent().toLowerCase(Locale.ROOT).contains("bruh"))
            {
                event.getMessage().reply("https://tenor.com/view/bruh-phone-drop-micdrop-ded-dead-gif-4568216");
            }

            if (event.getMessageContent().toLowerCase(Locale.ROOT).contains("mario"))
            {
                event.getChannel().sendMessage("https://media.discordapp.net/attachments/819840691421184002/847899529273278524/image0.gif");
            }

            if (event.getMessageContent().toLowerCase(Locale.ROOT).contains("deez nuts"))
            {
                event.getChannel().sendMessage("https://tenor.com/view/got-em-excited-laugh-gif-14755135");
            }

            if (event.getMessageContent().toLowerCase(Locale.ROOT).contains("burnt") || event.getMessageContent().toLowerCase(Locale.ROOT).contains("roasted"))
            {
                event.getChannel().sendMessage("https://tenor.com/view/roasted-oh-shookt-gif-8269968");
            }

            if (event.getMessageContent().toLowerCase(Locale.ROOT).contains("lmao") && !event.getMessageContent().toLowerCase(Locale.ROOT).startsWith(";lmaocount"))
            {
                event.getMessage().addReaction("🤣");
                lmaoCount++;
            }
        }
    }

    public void doStuff(MessageCreateEvent event)
    {
        String cmd = event.getMessageContent().toLowerCase(Locale.ROOT).substring(1);

        if (cmd.startsWith("remindme"))
            remindUser(event);
        else if (cmd.startsWith("del"))
            delete(event);

        switch (cmd)
        {
            case "sup":
            case "talk":
                talk();
                break;

            case "pfp":
            case "avatar":
                avatar(event);
                break;

            case "cya":
            case "bye":
                bye();
                break;

            case "help":
                help(event);
                break;

            case "lmaocount":
                event.getChannel().sendMessage("The \"lmao\" count is currently at: " + lmaoCount + "!");
                break;

        }
    }

    public void help(MessageCreateEvent event)
    {
        EmbedBuilder embed = new EmbedBuilder()
                .setTitle("Commands!")
                .setDescription("[Somewhat] detailed description of this bot's commands!")
                .addInlineField(";sup", "The bot says Hi!")
                .addInlineField(";pfp", "Sends user's avatar")
                .addInlineField(";bye", "Bye")
                .addInlineField(";remindme", "Reminds the sender in the given time. ")
                .setColor(Color.YELLOW)
                .setFooter("Please DM TheSj#3021 for any bugs/suggestions!");

        ch.sendMessage(embed);
    }

    public void delete(MessageCreateEvent event)
    {
        try
        {
            String[] message = event.getMessageContent().split(" ");
            int numOfMessages = Integer.parseInt(message[1]);
            //  for (int i = 0; i < numOfMessages; i++)
            {
                event.getChannel().bulkDelete(numOfMessages);
            }
        }
        catch (Exception e)
        {
            event.getChannel().sendMessage("Error, couldn't delete! Try again!");
        }

    }
    public void remindUser(MessageCreateEvent event)
    {
        String command = event.getMessageContent().replace(";remindme ", "");
        int number = -1;

        String[] values = command.split(" ");

        try
        {
            boolean flag = false;
            number = Integer.parseInt(values[0]);
            ReminderListener rem = new ReminderListener(event, values);

            if (values[1].contains("sec"))
            {
                number = number*100;
                flag = true;
            }
            else if (values[1].contains("min")) //if is in minutes
            {
                number = number*60*100;
                flag = true;
            }
            else if (values[1].contains("hr") || values[1].contains("hour")) //if is in minutes
            {
                number = number*3600*100;
                flag = true;
            }
            else if (values[1].contains("day"))
            {
                number = number*86400*100;
                flag = true;
            }

            if (flag)
            {
                Timer t = new Timer(number*10, rem);
                t.setRepeats(false);
                t.start();

                msg.addReaction("👍");
            }
        }
        catch(Exception e)
        {
            ch.sendMessage("Error! Couldn't create reminder. ");
        }
    }

    public void talk()
    {
        ch.sendMessage("Hello :smiley:");
    }

    public void avatar(MessageCreateEvent event)
    {
        EmbedBuilder embed = new EmbedBuilder()
                .setTitle(event.getMessageAuthor().getDisplayName())
                .setDescription("Avatar")
                .setColor(Color.YELLOW)
                .setImage(msg.getAuthor().getAvatar());

        ch.sendMessage(embed);
    }

    public void bye()
    {
        ch.sendMessage("Bye! :wave:");
        msg.addReaction("👋");
    }
}

class ReminderListener implements ActionListener
{
    MessageCreateEvent event;
    String[] text;

    public ReminderListener(MessageCreateEvent _event, String[] _text)
    {
        event = _event;
        text = _text;
    }

    public void actionPerformed(ActionEvent evt)
    {
        String desc = "";

        for (int i = 2; i < text.length; i++) {
            desc += text[i] + " ";
        }

        event.getMessage().reply(
                new EmbedBuilder()
                        .setTitle(event.getMessageAuthor().getDisplayName() + "'s reminder!!")
                        .setThumbnail(event.getMessageAuthor().getAvatar())
                        .setDescription(desc)
                        .setColor(Color.YELLOW)
        );
    }
}
