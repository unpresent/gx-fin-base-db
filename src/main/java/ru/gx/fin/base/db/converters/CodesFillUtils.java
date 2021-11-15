package ru.gx.fin.base.db.converters;

import org.jetbrains.annotations.NotNull;
import ru.gx.fin.base.db.dto.AbstractInstrument;
import ru.gx.fin.base.db.dto.InstrumentCode;
import ru.gx.fin.base.db.entities.AbstractInstrumentEntity;
import ru.gx.fin.base.db.entities.InstrumentCodeEntity;
import ru.gx.fin.base.db.entities.ProviderEntity;

public abstract class CodesFillUtils {

    public static void fillDtoCodes(
            @NotNull final AbstractInstrument destination,
            @NotNull final AbstractInstrumentEntity source,
            @NotNull final ProviderDtoFromEntityConverter providerDtoFromEntityConverter
    ) throws Exception {
        final var destCodes = destination.getCodes();
        final var sourceCodes = source.getCodes();
        for (var sourceCode : sourceCodes) {
            final var sourceProvider = sourceCode.getProvider();
            if (sourceProvider == null) {
                throw new Exception("It isn't allowed create InstrumentCode with null Provider; sourceCode = " + sourceCode);
            }
            final var provider = providerDtoFromEntityConverter.findDtoBySource(sourceCode.getProvider());
            if (provider == null) {
                throw new Exception("Can't find in memory Provider by ProviderEntity; sourceProvider = " + sourceProvider);
            }

            final var code = new InstrumentCode(
                    provider,
                    sourceCode.getCode(),
                    sourceCode.getName(),
                    sourceCode.getIndex(),
                    sourceCode.getDateFrom(),
                    sourceCode.getDateTo()
            );
            destCodes.add(code);
        }
    }

    public static void fillEntityCodes(
        @NotNull final AbstractInstrumentEntity destination,
        @NotNull final AbstractInstrument source,
        @NotNull final ProviderEntityFromDtoConverter providerEntityFromDtoConverter
    ) throws Exception {
        final var sourceCodes = source.getCodes();
        final var destCodes = destination.getCodes();
        destCodes.clear();
        for (var sourceCode : sourceCodes) {
            final var provider = providerEntityFromDtoConverter.findDtoBySource(sourceCode.getProvider());
            final var destCode = new InstrumentCodeEntity()
                    .setProvider(provider)
                    .setCode(sourceCode.getCode())
                    .setName(sourceCode.getName())
                    .setIndex(sourceCode.getIndex())
                    .setDateFrom(sourceCode.getDateFrom())
                    .setDateTo(sourceCode.getDateTo());
            destCodes.add(destCode);
        }
    }
}
