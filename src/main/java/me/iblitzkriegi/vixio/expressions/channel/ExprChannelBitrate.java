package me.iblitzkriegi.vixio.expressions.channel;

import ch.njol.skript.classes.Changer;
import me.iblitzkriegi.vixio.Vixio;
import me.iblitzkriegi.vixio.changers.ChangeableSimplePropertyExpression;
import me.iblitzkriegi.vixio.util.Util;
import me.iblitzkriegi.vixio.util.wrapper.Bot;
import net.dv8tion.jda.core.entities.VoiceChannel;
import org.bukkit.event.Event;

public class ExprChannelBitrate extends ChangeableSimplePropertyExpression<VoiceChannel, Integer> {
    static {
        Vixio.getInstance().registerPropertyExpression(ExprChannelBitrate.class, Integer.class,
                "bitrate", "voicechannels")
                .setName("Bitrate of voice channel")
                .setDesc("Get the bitrate of a voice channel." +
                        " The default value is 64kbps for channel builders. Rates multiplied by 1000." +
                        " You can set or reset this (resets to 64kbps)")
                .setExample(
                        "command /bitrate <string> <number>:",
                        "\ttrigger:",
                        "\t\tset bitrate of voice channel with id arg-1 to arg-2"
                );
    }

    @Override
    public Integer convert(VoiceChannel channel) {
        return channel.getBitrate() / 1000;
    }

    @Override
    protected String getPropertyName() {
        return "bitrate";
    }

    @Override
    public Class<? extends Integer> getReturnType() {
        return Integer.class;
    }

    @Override
    public Class<?>[] acceptChange(Changer.ChangeMode mode, boolean vixioChanger) {
        if (mode == Changer.ChangeMode.SET || mode == Changer.ChangeMode.RESET)
            return new Class[]{Number.class};
        return null;
    }

    @Override
    public boolean shouldError() {
        return false;
    }

    @Override
    public void change(Event e, Object[] delta, Bot bot, Changer.ChangeMode mode) {
        for (VoiceChannel channel : getExpr().getAll(e)) {
            channel = Util.bindVoiceChannel(bot, channel);
            channel.getManager().setBitrate(mode == Changer.ChangeMode.SET ?
                    ((Number) delta[0]).intValue() * 1000 : Util.DEFAULT_BITRATE)
                    .queue();
        }
    }

}