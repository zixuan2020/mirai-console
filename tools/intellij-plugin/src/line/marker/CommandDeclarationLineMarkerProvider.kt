/*
 * Copyright 2019-2021 Mamoe Technologies and contributors.
 *
 *  此源代码的使用受 GNU AFFERO GENERAL PUBLIC LICENSE version 3 许可证的约束, 可以在以下链接找到该许可证.
 *  Use of this source code is governed by the GNU AGPLv3 license that can be found through the following link.
 *
 *  https://github.com/mamoe/mirai/blob/master/LICENSE
 */

package net.mamoe.mirai.console.intellij.line.marker

import com.intellij.codeInsight.daemon.LineMarkerInfo
import com.intellij.codeInsight.daemon.LineMarkerProvider
import com.intellij.openapi.editor.markup.GutterIconRenderer
import com.intellij.psi.PsiElement
import net.mamoe.mirai.console.intellij.Icons
import net.mamoe.mirai.console.intellij.resolve.getElementForLineMark
import net.mamoe.mirai.console.intellij.resolve.isSimpleCommandHandlerOrCompositeCommandSubCommand
import net.mamoe.mirai.console.intellij.util.runIgnoringErrors
import org.jetbrains.kotlin.psi.KtNamedFunction

class CommandDeclarationLineMarkerProvider : LineMarkerProvider {
    override fun getLineMarkerInfo(element: PsiElement): LineMarkerInfo<*>? {
        if (element !is KtNamedFunction) return null
        if (!element.isSimpleCommandHandlerOrCompositeCommandSubCommand()) return null
        runIgnoringErrors { // not showing icons is better than throwing exception every time doing inspection
            return Info(getElementForLineMark(element.funKeyword ?: element.identifyingElement ?: element))
        }
    }

    @Suppress("DEPRECATION")
    class Info(callElement: PsiElement) : LineMarkerInfo<PsiElement>(
        callElement,
        callElement.textRange,
        Icons.CommandDeclaration,
        tooltipProvider,
        null, GutterIconRenderer.Alignment.RIGHT
    ) {
        companion object {
            val tooltipProvider = { _: PsiElement ->
                "子指令定义"
            }
        }
    }
}