package com.forgeessentials.worldborder.effect;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.DamageSource;

import com.forgeessentials.util.PlayerInfo;
import com.forgeessentials.worldborder.WorldBorder;
import com.forgeessentials.worldborder.WorldBorderEffect;

public class EffectDamage extends WorldBorderEffect
{

    private static final int INTERVAL = 1000;

    private int damage = 1;

    @Override
    public void tick(WorldBorder border, EntityPlayerMP player)
    {
        PlayerInfo pi = PlayerInfo.get(player);
        if (pi.checkTimeout(this.getClass().getName()))
        {
            player.attackEntityFrom(DamageSource.outOfWorld, damage);
            pi.startTimeout(this.getClass().getName(), INTERVAL);
        }
    }

}
