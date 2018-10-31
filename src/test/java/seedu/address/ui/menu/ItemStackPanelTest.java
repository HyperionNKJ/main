package seedu.address.ui.menu;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static seedu.address.ui.testutil.GuiTestAssert.assertStackPanelDisplaysItem;

import java.util.Map;

import org.junit.Test;

import guitests.guihandles.menu.ItemStackPanelHandle;
import seedu.address.model.menu.Item;
import seedu.address.testutil.menu.ItemBuilder;
import seedu.address.ui.GuiUnitTest;

public class ItemStackPanelTest extends GuiUnitTest {

    @Test
    public void display() {
        // no tags, recipe & required ingredients
        Item itemWithNoTags = new ItemBuilder().build();
        ItemStackPanel itemStackPanel = new ItemStackPanel(itemWithNoTags);
        uiPartRule.setUiPart(itemStackPanel);
        assertStackPanelDisplay(itemStackPanel, itemWithNoTags);

        // with tags
        Item itemWithTags = new ItemBuilder().withTags("husband", "friends").build();
        itemStackPanel = new ItemStackPanel(itemWithTags);
        uiPartRule.setUiPart(itemStackPanel);
        assertStackPanelDisplay(itemStackPanel, itemWithTags);

        // with recipe
        Item itemWithRecipe = new ItemBuilder().withRecipe("Some recipe").build();
        itemStackPanel = new ItemStackPanel(itemWithRecipe);
        uiPartRule.setUiPart(itemStackPanel);
        assertStackPanelDisplay(itemStackPanel, itemWithRecipe);

        // with required ingredients
        Item itemWithRequiredIngredients = new ItemBuilder()
                .withRequiredIngredients(Map.of("Apple", "3")).build();
        itemStackPanel = new ItemStackPanel(itemWithRequiredIngredients);
        uiPartRule.setUiPart(itemStackPanel);
        assertStackPanelDisplay(itemStackPanel, itemWithRequiredIngredients);

        // with tags, recipe & required ingredients
        Item item = new ItemBuilder().withRecipe("Other recipe")
                .withRequiredIngredients(Map.of("Apple", "3"))
                .withTags("friends").build();
        itemStackPanel = new ItemStackPanel(item);
        uiPartRule.setUiPart(itemStackPanel);
        assertStackPanelDisplay(itemStackPanel, item);
    }

    @Test
    public void equals() {
        Item item = new ItemBuilder().build();
        ItemStackPanel itemStackPanel = new ItemStackPanel(item);

        // same item, same index -> returns true
        ItemStackPanel copy = new ItemStackPanel(item);
        assertTrue(itemStackPanel.equals(copy));

        // same object -> returns true
        assertTrue(itemStackPanel.equals(itemStackPanel));

        // null -> returns false
        assertFalse(itemStackPanel.equals(null));

        // different types -> returns false
        assertFalse(itemStackPanel.equals(0));

        // different item, same index -> returns false
        // different name
        Item differentItem = new ItemBuilder().withName("differentName").build();
        assertFalse(itemStackPanel.equals(new ItemStackPanel(differentItem)));

        // different price
        differentItem = new ItemBuilder().withPrice("999").build();
        assertFalse(itemStackPanel.equals(new ItemStackPanel(differentItem)));

        // different tags
        differentItem = new ItemBuilder().withTags("different").build();
        assertFalse(itemStackPanel.equals(new ItemStackPanel(differentItem)));
    }

    /**
     * Asserts that {@code itemStackPanel} displays the details of {@code expectedItem} correctly.
     */
    private void assertStackPanelDisplay(ItemStackPanel itemStackPanel, Item expectedItem) {
        guiRobot.pauseForHuman();

        ItemStackPanelHandle itemStackPanelHandle = new ItemStackPanelHandle(itemStackPanel.getRoot());

        // verify item details are displayed correctly
        assertStackPanelDisplaysItem(expectedItem, itemStackPanelHandle);
    }
}
