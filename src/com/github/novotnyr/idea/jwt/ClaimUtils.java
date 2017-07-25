package com.github.novotnyr.idea.jwt;

import com.auth0.jwt.interfaces.Claim;
import com.github.novotnyr.idea.jwt.core.BooleanClaim;
import com.github.novotnyr.idea.jwt.core.DateClaim;
import com.github.novotnyr.idea.jwt.core.NamedClaim;
import com.github.novotnyr.idea.jwt.core.NumericClaim;
import com.github.novotnyr.idea.jwt.core.StringClaim;

import java.util.Date;

public class ClaimUtils {

    public static NamedClaim<?> getClaim(String claimName, Claim claimValue) {
        Date date = claimValue.asDate();
        if (isDateClaim(claimName, claimValue)) {
            return new DateClaim(claimName, date);
        }
        Boolean bool = claimValue.asBoolean();
        if(bool != null) {
            return new BooleanClaim(claimName, bool);
        }
        Long longClaim = claimValue.asLong();
        if(longClaim != null) {
            return new NumericClaim(claimName, longClaim);
        }
        return new StringClaim(claimName, claimValue.asString());

    }

    public static boolean isDateClaim(String claimName, Claim claimValue) {
        return claimValue.getClass() != null
                && Configuration.INSTANCE.getTimestampDateFields().contains(claimName);
    }

    public static NamedClaim<?> copyClaim(NamedClaim<?> templateClaim, String name, Object value) {
        if(templateClaim instanceof DateClaim) {
            Date date = null;
            if(value == null) {
                date = null;
            }
            if(value instanceof String) {
                date = DateUtils.toDate((String) value);
            } else if (value instanceof Long) {
                date = new Date((Long) value * 1000);
            } else if (value instanceof Date) {
                date = (Date) value;
            }
            return new DateClaim(name, date);
        }
        if(templateClaim instanceof BooleanClaim) {
            return new BooleanClaim(name, (Boolean) value);
        }
        if(templateClaim instanceof NumericClaim) {
            return new NumericClaim(name, (Long) value);
        }
        if(templateClaim instanceof StringClaim) {
            return new StringClaim(name, (String) value);
        }
        throw new IllegalArgumentException("Cannot convert " + value.getClass() + " to claim value");
    }

}
