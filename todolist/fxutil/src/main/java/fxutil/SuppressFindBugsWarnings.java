package fxutil;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Annontation to suppress spotbugs warnings.
 * Must have this specific name to be considered by spotbugs.
 */
@Retention(RetentionPolicy.CLASS)
public @interface SuppressFindBugsWarnings {
  /**
   * The set of FindBugs warnings that are to be suppressed in
   * annotated element. The value can be a bug category, kind or pattern.
   */
  String[] value() default {};

  /**
   * Optional documentation of the reason why the warning is suppressed.
   */
  String justification() default "";
}
