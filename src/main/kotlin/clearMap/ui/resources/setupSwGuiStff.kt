package clearMap.ui.resources

import sguiSwing.PrimaryIcon
import sguiSwing.SwPrimaryIconSet
import sguiSwing.SwProvider


fun setupSwGuiStuff() {
    SwProvider.converter = EngineLaunchpoint.converter

    SwPrimaryIconSet.setIcon(PrimaryIcon.SmallExpanded, ClearmapIcons.SmallIcons.Expanded)
    SwPrimaryIconSet.setIcon(PrimaryIcon.SmallExpandedHighlighted, ClearmapIcons.SmallIcons.ExpandedHighlighted)
    SwPrimaryIconSet.setIcon(PrimaryIcon.SmallUnexpanded, ClearmapIcons.SmallIcons.Unexpanded)
    SwPrimaryIconSet.setIcon(PrimaryIcon.SmallUnexpandedHighlighted, ClearmapIcons.SmallIcons.UnexpandedHighlighted)

    SwPrimaryIconSet.setIcon(PrimaryIcon.SmallArrowN, ClearmapIcons.SmallIcons.ArrowN)
    SwPrimaryIconSet.setIcon(PrimaryIcon.SmallArrowS, ClearmapIcons.SmallIcons.ArrowS)
    SwPrimaryIconSet.setIcon(PrimaryIcon.SmallArrowE, ClearmapIcons.SmallIcons.ArrowE)
    SwPrimaryIconSet.setIcon(PrimaryIcon.SmallArrowW, ClearmapIcons.SmallIcons.ArrowW)
}