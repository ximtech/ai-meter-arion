${i18n("telegram.msg.meter.name")}: <b>${meterName}</b>
<#if meterReadings??>
${i18n("telegram.msg.meter.readings")}: ${meterReadings}
</#if>
<#if battery < 40>
<b>${i18n("telegram.msg.meter.low.bat.warning")}</b>: ${battery + '%'}
<#else>
${i18n("telegram.msg.meter.battery")}: ${battery + '%'}
</#if>
${i18n("telegram.msg.meter.date")}: ${date}