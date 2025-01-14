package com.oscill.controller.config;

import androidx.annotation.NonNull;

import com.oscill.controller.Oscill;
import com.oscill.controller.OscillProperty;
import com.oscill.types.Dimension;
import com.oscill.types.Range;
import com.oscill.types.Unit;

import static com.oscill.types.Dimension.PICO;

public class SamplingPeriod extends OscillProperty<Long> {

    private final CpuTickLength cpuTickLength;
    private final SamplesCount samplesCount;

    public SamplingPeriod(@NonNull Oscill oscill,
                          @NonNull CpuTickLength cpuTickLength,
                          @NonNull SamplesCount samplesCount) {
        super(oscill);

        this.cpuTickLength = cpuTickLength;
        cpuTickLength.addLinkedSetting(this);

        this.samplesCount = samplesCount;
        samplesCount.addLinkedSetting(this);
    }

    @NonNull
    private CpuTickLength getCpuTickLength() {
        return cpuTickLength;
    }

    @NonNull
    private SamplesCount getSamplesCount() {
        return samplesCount;
    }

    @NonNull
    @Override
    protected Unit requestRealValueUnit() {
        return new Unit(PICO, Unit.SECOND);
    }

    @NonNull
    @Override
    protected Integer requestNativeValue() throws Exception {
        return getOscill().getSamplingPeriod();
    }

    @NonNull
    @Override
    protected Range<Integer> requestNativePropertyRange() throws Exception {
        int min = getOscill().getSamplingMinPeriod();
        int max = Integer.MAX_VALUE;
        return new Range<>(min, max);
    }

    @Override
    protected Integer applyNativeValue(@NonNull Integer nativeValue) throws Exception {
        return getOscill().setSamplingPeriod(nativeValue);
    }

    @Override
    protected Integer realToNative(@NonNull Long realValue) {
        return Math.round(((float)realValue) / (float)getCpuTickLength().getRealValue()) * 256;
    }

    @Override
    protected Long nativeToReal(@NonNull Integer nativeValue) {
        return ((long) (nativeValue / 256)) * getCpuTickLength().getRealValue();
    }

    public void setSamplingPeriod(float divTime, @NonNull Dimension timeDim) throws Exception {
        float samplingTime = timeDim.toDimension(divTime / (float) getSamplesCount().getSamplesByDivCount(), PICO);
        setRealValue((long)samplingTime);
    }

    public float getDivTime(@NonNull Dimension timeDim) {
        return PICO.toDimension(getRealValue() * getSamplesCount().getSamplesByDivCount(), timeDim);
    }

    public float getSampleTime(@NonNull Dimension timeDim) {
        return PICO.toDimension(getRealValue(), timeDim);
    }

}
