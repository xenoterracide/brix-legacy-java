import net.ltgt.gradle.errorprone.errorprone
import org.gradle.accessors.dm.LibrariesForChecker

plugins {
  `java-library`
  `java-test-fixtures`
  id("brix.bom")
  id("net.ltgt.errorprone")
}

val checker = the<LibrariesForChecker>()

dependencies {
  errorprone("com.google.errorprone:error_prone_core:2.+")
  errorprone("com.uber.nullaway:nullaway:0.+")
  compileOnly(checker.annotations)
  testFixturesCompileOnly(checker.annotations)
  testCompileOnly(checker.annotations)
}

java {
  toolchain {
    languageVersion.set(JavaLanguageVersion.of(11))
  }
}

tasks.withType<JavaCompile>().configureEach {
  options.compilerArgs.addAll(
    listOf(
      "-parameters",
      "-Xlint:deprecation",
      "-Xlint:unchecked"
    )
  )

  options.errorprone {
    disableWarningsInGeneratedCode.set(true)
    excludedPaths.set(".*/build/generated/sources/annotationProcessor/.*")
    option("NullAway:AnnotatedPackages", "com.xenoterracide")
    val errors = mutableListOf(
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
      "StreamResourceLeak",
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
      "UnnecessaryParentheses", // sketchy
      "UnsafeFinalization",
      "UnsafeReflectiveConstructionCast",
      "UnusedVariable",
      "UnusedMethod",
      "UnusedNestedClass",
      "UseCorrectAssertInTests",
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
      "StaticQualifiedUsingExpression",
      "AnnotationPosition",
      "AssertFalse",
      "CheckedExceptionNotThrown",
      "EmptyTopLevelDeclaration",
      "EqualsBrokenForNull",
      "ExpectedExceptionChecker",
      "InconsistentOverloads",
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
      "WildcardImport",
      "Var"
    )

    // if (name != "compileTestJava") errors.add("NullAway")
    if (name == "compileTestJava") option(
      "NullAway:ExcludedFieldAnnotations",
      "org.springframework.beans.factory.annotation.Autowired"
    )
    error(*errors.toTypedArray())
  }
}
