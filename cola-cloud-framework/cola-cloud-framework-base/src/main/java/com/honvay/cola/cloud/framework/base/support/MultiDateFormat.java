package com.honvay.cola.cloud.framework.base.support;

import java.text.DateFormat;
import java.text.FieldPosition;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * @author LIQIU
 * @date 2018-1-4
 **/
public class MultiDateFormat extends DateFormat {

    private List<SimpleDateFormat> dateFormats = new ArrayList<>(0);

    /**
     * Instantiates a new Multi date format.
     *
     * @param formats the formats
     */
    public MultiDateFormat(String... formats) {
        if (formats == null || formats.length == 0) {
            dateFormats = Arrays.asList(
                    new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"),
                    new SimpleDateFormat("yyyy-MM-dd")
            );
        } else {
            for (String format : formats) {
                dateFormats.add(new SimpleDateFormat(format));
            }
        }
    }

    @Override
    public StringBuffer format(Date date, StringBuffer toAppendTo, FieldPosition fieldPosition) {
        for (final DateFormat dateFormat : dateFormats) {
            try{
                String res;
                if ((res = dateFormat.format(date)) != null) {
                    toAppendTo.append(res);
                    return toAppendTo;
                }
            }catch (Exception e){

            }
        }
        return toAppendTo;
    }

    @Override
    public Date parse(String source, ParsePosition pos) {
        Date res = null;
        for (final DateFormat dateFormat : dateFormats) {
            if ((res = dateFormat.parse(source, pos)) != null) {
                return res;
            }
        }

        return null;
    }
}
