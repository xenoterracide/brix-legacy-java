import com.github.spotbugs.snom.Confidence
import com.github.spotbugs.snom.Effort
import com.github.spotbugs.snom.SpotBugsTask
import net.ltgt.gradle.errorprone.CheckSeverity
import net.ltgt.gradle.errorprone.errorprone
import net.ltgt.gradle.nullaway.nullaway
import org.gradle.api.tasks.testing.logging.TestLogEvent

plugins {
  application
  checkstyle
  distribution
  // NOTE: external plugin version is specified in implementation dependency artifact of the project's build file
  id("com.github.spotbugs").version("4.6.0")
  id("com.diffplug.spotless").version("5.8.2")
  id("net.ltgt.errorprone").version("1.3.0")
  id("net.ltgt.nullaway").version("1.0.2")
  id("de.inetsoftware.setupbuilder").version("4.8.7")
  id("org.checkerframework").version("0.5.13")
  id("com.xenoterracide.gradle.sem-ver").version("0.7.10")
}

group = "com.xenoterracide"
version = "0.1.0-SNAPSHOT"

repositories {
  mavenCentral()
}

dependencyLocking {
  lockAllConfigurations()
}

dependencies {
  annotationProcessor("org.immutables:value:2.+")
  compileOnly("org.immutables:value-annotations:2.+")
  errorprone("com.google.errorprone:error_prone_core:2.4.+")
  errorprone("com.uber.nullaway:nullaway:0.8.+")
  checkerFramework("org.checkerframework:checker:3.+")
  compileOnly("org.checkerframework:checker-qual:3.+")
  implementation(platform("org.apache.logging.log4j:log4j-bom:2.+"))
  runtimeOnly("org.apache.logging.log4j:log4j-slf4j18-impl")
  implementation("org.apache.logging.log4j:log4j-core")
  implementation("org.apache.logging.log4j:log4j-api")
  implementation(platform("com.fasterxml.jackson:jackson-bom:2.+"))
  implementation("com.fasterxml.jackson.core:jackson-core")
  implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-yaml")
  runtimeOnly("com.fasterxml.jackson.core:jackson-databind")
  runtimeOnly("com.fasterxml.jackson.module:jackson-module-parameter-names")
  implementation("org.apache.commons:commons-lang3:3.+")
  implementation("io.pebbletemplates:pebble:3.+")
  implementation("io.vavr:vavr:0.+")
  implementation("commons-io:commons-io:2.+")
  implementation("info.picocli:picocli:4.+")
  testImplementation(platform("org.junit:junit-bom:5.+"))
  testImplementation("org.junit.jupiter:junit-jupiter-params")
  testImplementation("org.assertj:assertj-core:3.+")
  testImplementation("org.mockito:mockito-core:3.+")
  testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
}

application {
  mainClass.set("com.xenoterracide.brix.Application")
}

tasks.withType<Jar> {
  manifest {
    attributes(
      "Class-Path" to configurations.compile.get().joinToString(" ") { it.name },
      "Main-Class" to "com.xenoterracide.brix.Application"
    )
  }
}

java {
  toolchain {
    languageVersion.set(JavaLanguageVersion.of(11))
  }
}

tasks.test {
  useJUnitPlatform()

  testLogging {
    lifecycle {
      showStandardStreams = true
      displayGranularity = 2
      events.addAll(listOf(TestLogEvent.STARTED, TestLogEvent.PASSED, TestLogEvent.SKIPPED, TestLogEvent.FAILED))
    }
  }
  reports {
    html.isEnabled = false
    junitXml.isEnabled = false
  }
}

tasks.withType<Checkstyle>().configureEach {
  isShowViolations = true
  reports {
    html.isEnabled = false
    xml.isEnabled = false
  }
}

tasks.named<Checkstyle>("checkstyleMain") {
  configFile = file("config/checkstyle/main.xml")
}

tasks.named<Checkstyle>("checkstyleTest") {
  configFile = file("config/checkstyle/test.xml")
}

checkerFramework {
  excludeTests = true
  extraJavacArgs.addAll(listOf("-Astubs=$buildDir/../config/stubs"))
  checkers.addAll(
    listOf(
      "org.checkerframework.checker.nullness.NullnessChecker"
    )
  )
}

spotbugs {
  excludeFilter.set(file("./config/spotbugs/exclude.xml"))
  effort.set(Effort.MAX)
  reportLevel.set(Confidence.LOW)
}

tasks.withType<SpotBugsTask>().configureEach {
  reports.register("html") {
    enabled = true
  }
}

val copyright = "Copyright © \$YEAR Caleb Cushing."
val license = "Apache 2.0. See https://github.com/xenoterracide/brix/LICENSE"
val licenseSimple = "https://choosealicense.com/licenses/apache-2.0/#"
spotless {
  ratchetFrom = "HEAD"
  java {
    licenseHeader(
  "/*\n" +
      "* $copyright\n" +
      "* $license\n" +
      "* $licenseSimple\n" +
      "*/"
    )
  }
}

nullaway {
  annotatedPackages.add("com.xenoterracide")
}

tasks.withType<JavaCompile>().configureEach {
  options.compilerArgs.addAll(
    listOf(
      "-parameters",
      "-Werror",
      "-Xlint:deprecation",
      "-Xlint:unchecked"
    )
  )

  options.errorprone {
    nullaway {
      severity.set(CheckSeverity.ERROR)
      acknowledgeRestrictiveAnnotations.set(true)
      handleTestAssertionLibraries.set(true)
      checkOptionalEmptiness.set(true)
    }
    disableWarningsInGeneratedCode.set(true)
    excludedPaths.set(".*/build/generated/sources/annotationProcessor/.*")
    error(
      "AmbiguousMethodReference",
      "ArgumentSelectionDefectChecker",
      "ArrayAsKeyOfSetOrMap",
      "AssertEqualsArgumentOrderChecker",
      "AssertThrowsMultipleStatements",
      "AssertionFailureIgnored",
      "AssignmentToMock",
      "BadComparable",
      "BadImport",
      "BadInstanceof",
      "BigDecimalEquals",
      "BigDecimalLiteralDouble",
      "BoxedPrimitiveConstructor",
      "BoxedPrimitiveEquality",
      "ByteBufferBackingArray",
      "CacheLoaderNull",
      "CannotMockFinalClass",
      "CanonicalDuration",
      "CatchFail",
      "CatchAndPrintStackTrace",
      "ClassCanBeStatic",
      "ClassNewInstance", // sketchy
      "CollectionUndefinedEquality",
      "CollectorShouldNotUseState",
      "ComparableAndComparator",
      "CompareToZero",
      "ComplexBooleanConstant",
      "DateFormatConstant",
      "DefaultCharset",
      "DefaultPackage",
      "DoubleBraceInitialization",
      "DoubleCheckedLocking",
      "EmptyCatch",
      "EqualsGetClass",
      "EqualsIncompatibleType",
      "EqualsUnsafeCast",
      "EqualsUsingHashCode",
      "ExtendingJUnitAssert",
      "FallThrough",
      "Finally",
      "FloatCast",
      "FloatingPointLiteralPrecision",
      "FutureReturnValueIgnored",
      "GetClassOnEnum",
      "HidingField",
      "ImmutableAnnotationChecker",
      "ImmutableEnumChecker",
      "InconsistentCapitalization",
      "InconsistentHashCode",
      "IncrementInForLoopAndHeader",
      "InlineFormatString",
      "InputStreamSlowMultibyteRead",
      "InstanceOfAndCastMatchWrongType",
      "InvalidThrows",
      "IterableAndIterator",
      "JavaDurationGetSecondsGetNano",
      "JavaDurationWithNanos",
      "JavaDurationWithSeconds",
      "JavaInstantGetSecondsGetNano",
      "JavaLangClash",
      "JavaLocalDateTimeGetNano",
      "JavaLocalTimeGetNano",
      "JavaTimeDefaultTimeZone",
      // "JavaUtilDate",
      "LockNotBeforeTry",
      "LockOnBoxedPrimitive",
      "LogicalAssignment",
      "MissingCasesInEnumSwitch",
      "Overrides",
      "MissingOverride",
      "MixedMutabilityReturnType",
      "ModifiedButNotUsed",
      "ModifyCollectionInEnhancedForLoop",
      "ModifySourceCollectionInStream",
      "MultipleParallelOrSequentialCalls",
      "MultipleUnaryOperatorsInMethodCall",
      "MutableConstantField",
      "MutablePublicArray",
      "NestedInstanceOfConditions",
      "NonAtomicVolatileUpdate",
      "NonOverridingEquals",
      "NullOptional",
      "NullableConstructor",
      "NullablePrimitive",
      "NullableVoid",
      "ObjectToString",
      "ObjectsHashCodePrimitive",
      "OperatorPrecedence",
      "OptionalMapToOptional",
      "OrphanedFormatString",
      "OverrideThrowableToString",
      "PreconditionsCheckNotNullRepeated",
      "PrimitiveAtomicReference",
      "ProtectedMembersInFinalClass",
      "PreconditionsCheckNotNullRepeated",
      "ReferenceEquality",
      "ReturnFromVoid",
      "RxReturnValueIgnored",
      "SameNameButDifferent",
      "ShortCircuitBoolean",
      "StaticAssignmentInConstructor",
      "StaticGuardedByInstance",
      // "StaticMockMember",
      "StreamResourceLeak",
      // "StreamToIterable",
      "StringSplitter",
      "SynchronizeOnNonFinalField",
      "ThreadJoinLoop",
      "ThreadLocalUsage",
      "ThreeLetterTimeZoneID",
      "TimeUnitConversionChecker",
      "ToStringReturnsNull",
      "TreeToString",
      "TypeEquals",
      "TypeNameShadowing",
      "TypeParameterShadowing",
      "TypeParameterUnusedInFormals", // sketchy
      "URLEqualsHashCode",
      "UndefinedEquals",
      "UnnecessaryAnonymousClass",
      "UnnecessaryLambda",
      "UnnecessaryMethodInvocationMatcher",
      // "UnnecessaryMethodReference",
      "UnnecessaryParentheses", // sketchy
      "UnsafeFinalization",
      "UnsafeReflectiveConstructionCast",
      "UnusedVariable",
      "UnusedMethod",
      "UnusedNestedClass",
      "UseCorrectAssertInTests",
      // "UseTimeInScope",
      "VariableNameSameAsType",
      "WaitNotInLoop",
      "ClassName",
      "ComparisonContractViolated",
      "DeduplicateConstants",
      "DivZero",
      "EmptyIf",
      "FuzzyEqualsShouldNotBeUsedInEqualsMethod",
      "IterablePathParameter",
      "LongLiteralLowerCaseSuffix",
      "NumericEquality",
      "ParameterPackage",
      "StaticQualifiedUsingExpression",
      "AnnotationPosition",
      "AssertFalse",
      "CheckedExceptionNotThrown",
      // "DifferentNameButSame",
      "EmptyTopLevelDeclaration",
      "EqualsBrokenForNull",
      "ExpectedExceptionChecker",
      "InconsistentOverloads",
      // "InitializeInline",
      "InterruptedExceptionSwallowed",
      "InterfaceWithOnlyStatics",
      "NonCanonicalStaticMemberImport",
      "PreferJavaTimeOverload",
      "ClassNamedLikeTypeParameter",
      "ConstantField",
      "FieldCanBeLocal",
      "FieldCanBeStatic",
      "ForEachIterable",
      "MethodCanBeStatic",
      "MultiVariableDeclaration",
      "MultipleTopLevelClasses",
      "PackageLocation",
      "RemoveUnusedImports",
      "ParameterNotNullable",
      "WildcardImport",
      "Var"
    )
  }
}

