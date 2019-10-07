package misc;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * A helper annotation for identifying things that should be ignored by FindBugs
 *
 * Copied from @link{https://sourceforge.net/p/findbugs/feature-requests/298/#5e88}
 */
@Retention(RetentionPolicy.CLASS)
public @interface SuppressFBWarnings {

  /**
   * The set of FindBugs warnings that are to be suppressed in annotated element. The value can be a
   * bug category, kind or pattern.
   */
  String[] value() default {};

  /**
   * Optional documentation of the reason why the warning is suppressed.
   */
  String justification() default "";
}