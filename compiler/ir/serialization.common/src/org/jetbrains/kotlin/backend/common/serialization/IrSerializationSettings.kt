/*
 * Copyright 2010-2024 JetBrains s.r.o. and Kotlin Programming Language contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the license/LICENSE.txt file.
 */

package org.jetbrains.kotlin.backend.common.serialization

import org.jetbrains.kotlin.config.LanguageVersionSettings
import org.jetbrains.kotlin.ir.IrFileEntry

/**
 * Various settings used during serialization of IR modules and IR files.
 *
 * @property compatibilityMode The compatibility mode for computing signatures. See [CompatibilityMode] for more details.
 * @property languageVersionSettings The language version settings.
 * @property publicAbiOnly Whether only the part of IR that comprises public ABI should be serialized.
 *   This setting is used for generating so-called "header KLIBs".
 * @property sourceBaseDirs The list of base paths (prefixes), which is used to compute a relative path to every absolute path
 *   stored in [IrFileEntry.name] before serializing this path to the IR file proto. If the list is empty, then computation of
 *   relative paths is not performed.
 * @property normalizeAbsolutePaths Whether absolute paths stored in [IrFileEntry.name] should be normalized
 *   (i.e. whether path separators should be replaced by '/') before serializing these paths to the IR file proto.
 *   Note: This transformation is only applied to those paths that were not relativized, i.e., have no common prefixes with [sourceBaseDirs].
 * @property bodiesOnlyForInlines Whether to serialize bodies of only inline functions. Effectively, this setting is only relevant to Kotlin/JVM.
 * @property shouldCheckSignaturesOnUniqueness Whether to run checks on uniqueness of generated signatures.
 */
class IrSerializationSettings(
    val compatibilityMode: CompatibilityMode = CompatibilityMode.CURRENT,
    val languageVersionSettings: LanguageVersionSettings,
    val publicAbiOnly: Boolean = false,
    val sourceBaseDirs: Collection<String> = emptyList(),
    val normalizeAbsolutePaths: Boolean = false,
    val bodiesOnlyForInlines: Boolean = false,
    val shouldCheckSignaturesOnUniqueness: Boolean = true,
)
