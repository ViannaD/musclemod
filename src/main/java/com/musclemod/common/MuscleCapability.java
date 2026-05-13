package com.musclemod.common;

import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.util.INBTSerializable;

public class MuscleCapability implements INBTSerializable<CompoundTag> {

    private boolean isMuscle = false;

    public boolean isMuscle() {
        return isMuscle;
    }

    public void setMuscle(boolean muscle) {
        this.isMuscle = muscle;
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();
        tag.putBoolean("isMuscle", isMuscle);
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        isMuscle = nbt.getBoolean("isMuscle");
    }
}
